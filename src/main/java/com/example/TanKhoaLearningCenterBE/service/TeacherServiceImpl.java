package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.TeacherDTO;
import com.example.TanKhoaLearningCenterBE.entity.AccountEntity;
import com.example.TanKhoaLearningCenterBE.entity.TeacherEntity;
import com.example.TanKhoaLearningCenterBE.exception.AccountAlreadyAssignedException;
import com.example.TanKhoaLearningCenterBE.exception.AccountNotFoundException;
import com.example.TanKhoaLearningCenterBE.exception.InvalidAccountRoleException;
import com.example.TanKhoaLearningCenterBE.repository.AccountRepository;
import com.example.TanKhoaLearningCenterBE.repository.TeacherRepository;
import com.example.TanKhoaLearningCenterBE.utils.user.Role;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateTeacherRequest;
import com.example.TanKhoaLearningCenterBE.controller.request.UpdateTeacherRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.FileUploadResponse;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final AccountRepository accountRepository;

    @Override
    public ResponseEntity<TeacherDTO> create(CreateTeacherRequest request) {
        var teac = new TeacherEntity();
        teac.setTeacherName(request.getName());
        teac.setTphoneNumber(request.getPhoneNumber());
        teac.setTEmail(request.getEmail());
        var saveTeacher = teacherRepository.save(teac);

        return ResponseEntity.status(HttpStatus.CREATED).body(new TeacherDTO(saveTeacher));
    }

    @Override
    public ResponseEntity<TeacherDTO> put(UUID id, UpdateTeacherRequest request) {
        Optional<TeacherEntity> teacherOptional = teacherRepository.findById(id);
        if (teacherOptional.isPresent()) {
            TeacherEntity teac = teacherOptional.get();
            if (!request.getTeacherName().isBlank()) {
                teac.setTeacherName(request.getTeacherName());
            }

            if (!request.getPhoneNumber().isBlank()) {
                teac.setTphoneNumber(request.getPhoneNumber());
            }

            if (!request.getEmail().isBlank()) {
                teac.setTEmail(request.getEmail());
            }

            if (request.getAccountId() != null) {
                Optional<AccountEntity> accountOptional = accountRepository.findById(request.getAccountId());
                if (accountOptional.isPresent()) {
                    AccountEntity account = accountOptional.get();
                    if (account.getRole() == Role.TEACHER) {
                        if (isAccountAssigned(account.getAccountId())) {
                            throw new AccountAlreadyAssignedException();
                        }
                        teac.setAccountIds(account);
                    } else {
                        throw new InvalidAccountRoleException();
                    }
                } else {
                    throw new AccountNotFoundException();
                }
            }

            var res = teacherRepository.save(teac);

            return ResponseEntity.ok(new TeacherDTO(res));
        }
        throw new RuntimeException("Teacher not found");
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            teacherRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new RuntimeException("Teacher not found");
    }

    @Override
    public ResponseEntity<TeacherDTO> get(UUID id) {
        Optional<TeacherEntity> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            return ResponseEntity.ok(new TeacherDTO(optionalTeacher.get()));
        }
        throw new RuntimeException("Teacher not found");
    }

    @Override
    public ResponseEntity<PageResponse<TeacherDTO>> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TeacherEntity> teachers = teacherRepository.findAll(pageable);
        List<TeacherDTO> rows = teachers.getContent().stream().map(TeacherDTO::new).toList();

        var response = new PageResponse<TeacherDTO>();
        response.setCount(teachers.getTotalElements());
        response.setRows(rows);
        response.setPage(page);
        response.setSize(size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<List<TeacherDTO>> search(String name) {
        List<TeacherEntity> teacherEntityList = teacherRepository.findByTeacherNameContainingIgnoreCase(name);
        if (!teacherEntityList.isEmpty()) {
            return ResponseEntity.ok(teacherEntityList.stream().map(TeacherDTO::new).toList());
        }
        throw new RuntimeException("Teacher not found");
    }

    private boolean isAccountAssigned(UUID accountId) {

        return teacherRepository.existsByAccountIds_AccountId(accountId);
    }

    @Override
    public ResponseEntity<FileUploadResponse> uploadTeachersFromExcel(MultipartFile file) {
        FileUploadResponse response = new FileUploadResponse();
        if (file.isEmpty()) {
            response.setMessage("Please Upload the file!");
            response.setSuccessfulCount(0);
            return ResponseEntity.badRequest().body(response);
        }
        try {
            List<CreateTeacherRequest> teachers = processExcelFile(file);
            // Kiểm tra xem có giáo viên nào được xử lý thành công không
            if (teachers.isEmpty()) {
                response.setMessage("No valid teachers found in the Excel file.");
                response.setSuccessfulCount(0);
                return ResponseEntity.badRequest().body(response);
            }
            saveTeachers(teachers);
            response.setMessage("Teachers uploaded successfully!");
            response.setSuccessfulCount(teachers.size());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.setMessage("Failed to upload teachers: " + e.getMessage());
            response.setSuccessfulCount(0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (IllegalArgumentException e) { // Bắt lỗi do dữ liệu không hợp lệ
            response.setMessage("Invalid data in Excel file: " + e.getMessage());
            response.setSuccessfulCount(0);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) { // Bắt các lỗi không mong muốn khác
            response.setMessage("An unexpected error occurred: " + e.getMessage());
            response.setSuccessfulCount(0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return ""; // Trả về chuỗi rỗng thay vì null
        }
        cell.setCellType(CellType.STRING);
        String value = cell.getStringCellValue().trim();
        return value;
    }

    @Override
    public List<CreateTeacherRequest> processExcelFile(MultipartFile file) throws IOException {
        List<CreateTeacherRequest> teachers = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Bỏ qua header row nếu có
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                CreateTeacherRequest teacherRequest = new CreateTeacherRequest();

                String name = getStringCellValue(row.getCell(0));
                String phone = getStringCellValue(row.getCell(1));
                String email = getStringCellValue(row.getCell(2));

                // Validate dữ liệu trước khi tạo đối tượng
                if (isValidTeacherData(name, phone, email)) {
                    teacherRequest.setName(name);
                    teacherRequest.setPhoneNumber(phone);
                    teacherRequest.setEmail(email);
                    teachers.add(teacherRequest);
                } else {
                    // Ném ngoại lệ để xử lý ở hàm uploadTeachersFromExcel
                    throw new IllegalArgumentException("Invalid data in row " + (row.getRowNum() + 1) +
                            ": Name='" + name + "', Phone='" + phone + "', Email='" + email + "'");
                }
            }
        }
        return teachers;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK && getStringCellValue(cell).trim() != "") {
                return false;
            }
        }
        return true;
    }

    private boolean isValidTeacherData(String name, String phone, String email) {
        // Thực hiện validate dữ liệu, trả về true nếu hợp lệ, false nếu không.
        // Ví dụ:
        if (name == null || name.isEmpty() || phone == null || phone.isEmpty() || email == null || email.isEmpty()) {
            return false;
        }
        //Thêm các điều kiện ràng buộc khác nếu cần
        return true;
    }

    @Override
    @Transactional
    public void saveTeachers(List<CreateTeacherRequest> teacherRequests) {
        List<TeacherEntity> teacherEntities = teacherRequests.stream()
                .map(this::convertToEntity) // Đổi tên cho thống nhất
                .collect(Collectors.toList());
        teacherRepository.saveAll(teacherEntities);
    }

    private TeacherEntity convertToEntity(CreateTeacherRequest request) { // Đổi tên cho thống nhất
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setTeacherName(request.getName());
        teacherEntity.setTphoneNumber(request.getPhoneNumber());
        teacherEntity.setTEmail(request.getEmail());
        return teacherEntity;
    }

    @Override
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        List<TeacherEntity> teachers = teacherRepository.findAll();
        List<TeacherDTO> teacherDTOS = teachers.stream().map(TeacherDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(teacherDTOS);
    }
}
