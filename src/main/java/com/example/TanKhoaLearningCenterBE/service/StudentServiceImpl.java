package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.StudentDTO;
import com.example.TanKhoaLearningCenterBE.entity.AccountEntity;
import com.example.TanKhoaLearningCenterBE.entity.BillDetailEntity;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import com.example.TanKhoaLearningCenterBE.exception.AccountAlreadyAssignedException;
import com.example.TanKhoaLearningCenterBE.exception.AccountNotFoundException;
import com.example.TanKhoaLearningCenterBE.exception.InvalidAccountRoleException;
import com.example.TanKhoaLearningCenterBE.exception.StudentNotFoundException;
import com.example.TanKhoaLearningCenterBE.repository.AccountRepository;
import com.example.TanKhoaLearningCenterBE.repository.BillDetailRepository;
import com.example.TanKhoaLearningCenterBE.repository.StudentRepository;
import com.example.TanKhoaLearningCenterBE.utils.user.Role;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateStudentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateStudentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;
    private final BillDetailRepository billDetailRepository;
    private final AccountRepository accountRepository;

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
}
