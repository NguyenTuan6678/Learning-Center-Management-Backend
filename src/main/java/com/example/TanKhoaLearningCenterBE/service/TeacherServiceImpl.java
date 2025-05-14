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
}
