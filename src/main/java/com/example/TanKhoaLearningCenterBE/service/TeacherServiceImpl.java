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
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateTeacherRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateTeacherRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.FileUploadResponse;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
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
            if (!request.getName().isBlank()) {
                teac.setTeacherName(request.getName());
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
                    if (account.getRole() == Role.STUDENT) {
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
    public ResponseEntity<FileUploadResponse> uploadStudentsFromExcel(MultipartFile file) {
        FileUploadResponse response = new FileUploadResponse();
        if (file.isEmpty()) {
            response.setMessage("Please Upload the file!");
            response.setSuccessfulCount(0);
            return ResponseEntity.badRequest().body(response);
        }
        try {
            List<CreateTeacherRequest> teachers = processExcelFile(file);
            saveTeachers(teachers);
            response.setMessage("Teacher uploaded successfully!");
            response.setSuccessfulCount(teachers.size());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.setMessage("Failed to upload teachers: " + e.getMessage());
            response.setSuccessfulCount(0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    @Override
    public List<CreateTeacherRequest> processExcelFile(MultipartFile file) throws IOException {
        List<CreateTeacherRequest> teachers = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                CreateTeacherRequest teacherRequest = new CreateTeacherRequest();

                Cell nameCell = row.getCell(0);
                Cell phoneCell = row.getCell(1);
                Cell emailCell = row.getCell(2);

                teacherRequest.setName(getStringCellValue(nameCell));
                teacherRequest.setPhoneNumber(getStringCellValue(phoneCell));
                teacherRequest.setEmail(getStringCellValue(emailCell));

                teachers.add(teacherRequest);
            }
        }
        return teachers;
    }

    @Override
    @Transactional
    public void saveTeachers(List<CreateTeacherRequest> teacherRequests) {
        List<TeacherEntity> teacherEntities = teacherRequests.stream()
                .map(this::converToEntity)
                .collect(Collectors.toList());
        teacherRepository.saveAll(teacherEntities);
    }

    private TeacherEntity converToEntity(CreateTeacherRequest request) {
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setTeacherName(request.getName());
        teacherEntity.setTphoneNumber(request.getPhoneNumber());
        teacherEntity.setTEmail(request.getEmail());

        return teacherEntity;
    }
}
