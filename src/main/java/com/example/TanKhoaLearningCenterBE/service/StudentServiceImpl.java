package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.StudentDTO;
import com.example.TanKhoaLearningCenterBE.entity.*;
import com.example.TanKhoaLearningCenterBE.exception.AccountAlreadyAssignedException;
import com.example.TanKhoaLearningCenterBE.exception.AccountNotFoundException;
import com.example.TanKhoaLearningCenterBE.exception.InvalidAccountRoleException;
import com.example.TanKhoaLearningCenterBE.exception.StudentNotFoundException;
import com.example.TanKhoaLearningCenterBE.repository.*;
import com.example.TanKhoaLearningCenterBE.utils.user.Role;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateStudentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateStudentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.FileUploadResponse;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;
    private final BillDetailRepository billDetailRepository;
    private final AccountRepository accountRepository;
    private final ClassRepository classRepository;
    private final ClassStudentRepository classStudentRepository;

    @Override
    public ResponseEntity<StudentDTO> create(CreateStudentRequest request) {
        var stud = new StudentEntity();
        stud.setStdName(request.getName());
        stud.setStdPhoneNumber(request.getPhoneNumber());
        stud.setStdEmail(request.getEmail());
        var saveStudent = studentRepository.save(stud);

        return ResponseEntity.status(HttpStatus.CREATED).body(new StudentDTO(saveStudent));
    }

    @Override
    public ResponseEntity<StudentDTO> put(UUID id, UpdateStudentRequest command) {
        Optional<StudentEntity> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            StudentEntity stud = studentOptional.get();
            if (!command.getName().isBlank()) {
                stud.setStdName(command.getName());
            }

            if (!command.getPhoneNumber().isBlank()) {
                stud.setStdPhoneNumber(command.getPhoneNumber());
            }

            if (!command.getEmail().isBlank()) {
                stud.setStdEmail(command.getEmail());
            }

            if (command.getAccountId() != null) {
                Optional<AccountEntity> accountOptional = accountRepository.findById(command.getAccountId());
                if (accountOptional.isPresent()) {
                    AccountEntity account = accountOptional.get();
                    if (account.getRole() == Role.STUDENT) {
                        if (isAccountAssigned(account.getAccountId()))
                        {
                            throw new AccountAlreadyAssignedException();
                        }
                        stud.setAccountIds(account);
                    } else {
                        throw new InvalidAccountRoleException();
                    }
                } else {
                    throw new AccountNotFoundException();
                }
            }

            var res = studentRepository.save(stud);

            return ResponseEntity.ok(new StudentDTO(res));
        }
        throw new StudentNotFoundException();
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        Optional<StudentEntity> optionalStudent = studentRepository.findById(id);
        List<BillDetailEntity> details = billDetailRepository.findAllByStudent_StudentId(id);
        for (BillDetailEntity detail : details) {
            detail.setStudent(null);
        }
        billDetailRepository.saveAll(details);
        if (optionalStudent.isPresent()) {
            studentRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new StudentNotFoundException();
    }

    @Override
    public ResponseEntity<PageResponse<StudentDTO>> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StudentEntity> students = studentRepository.findAll(pageable);
        List<StudentDTO> rows = students.getContent().stream().map(StudentDTO::new).toList();

        var response =  new PageResponse<StudentDTO>();
        response.setCount(students.getTotalElements());
        response.setRows(rows);
        response.setPage(page);
        response.setSize(size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<StudentDTO> get(UUID id) {
        Optional<StudentEntity> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            return ResponseEntity.ok(new StudentDTO(optionalStudent.get()));
        }
        throw new StudentNotFoundException();
    }

    @Override
    public ResponseEntity<List<StudentDTO>> search(String name) {
        List<StudentEntity> studentEntityList = studentRepository.findByStdNameContainingIgnoreCase(name);
        if (!studentEntityList.isEmpty()) {
            return ResponseEntity.ok(studentEntityList.stream().map(StudentDTO::new).toList());
        }
        throw new StudentNotFoundException();
    }

    private boolean isAccountAssigned(UUID accountId) {

        return studentRepository.existsByAccountIds_AccountId(accountId);
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
            List<CreateStudentRequest> students = processExcelFile(file);
            saveStudents(students);
            response.setMessage("Student uploaded successfully!");
            response.setSuccessfulCount(students.size());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.setMessage("Failed to upload students: " + e.getMessage());
            response.setSuccessfulCount(0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public List<CreateStudentRequest> processExcelFile(MultipartFile file) throws IOException {
        List<CreateStudentRequest> students = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                CreateStudentRequest studentRequest = new CreateStudentRequest();

                Cell nameCell = row.getCell(0);
                Cell phoneCell = row.getCell(1);
                Cell emailCell = row.getCell(2);

                studentRequest.setName(getStringCellValue(nameCell));
                studentRequest.setPhoneNumber(getStringCellValue(phoneCell));
                studentRequest.setEmail(getStringCellValue(emailCell));

                students.add(studentRequest);
            }
        }
        return students;
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    @Override
    @Transactional
    public void saveStudents(List<CreateStudentRequest> studentRequests) {
        List<StudentEntity> studentEntities = studentRequests.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        studentRepository.saveAll(studentEntities);
    }

    private StudentEntity convertToEntity(CreateStudentRequest request) {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setStdName(request.getName());
        studentEntity.setStdPhoneNumber(request.getPhoneNumber());
        studentEntity.setStdEmail(request.getEmail());

        return studentEntity;
    }
}
