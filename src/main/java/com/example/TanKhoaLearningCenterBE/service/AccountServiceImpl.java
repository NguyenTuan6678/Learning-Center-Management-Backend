package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.AccountDTO;
import com.example.TanKhoaLearningCenterBE.entity.AccountEntity;
import com.example.TanKhoaLearningCenterBE.exception.AccountNotFoundException;
import com.example.TanKhoaLearningCenterBE.exception.UserNameAlreadyExistException;
import com.example.TanKhoaLearningCenterBE.repository.AccountRepository;
import com.example.TanKhoaLearningCenterBE.repository.StudentRepository;
import com.example.TanKhoaLearningCenterBE.repository.TeacherRepository;
import com.example.TanKhoaLearningCenterBE.utils.user.Role;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateAccountRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateAccountRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final PasswordEncoder encoder;
    private final TeacherRepository teacherRepository;
    private final AccountRepository accountRepository;
    private final StudentRepository studentRepository;

    @Override
    public ResponseEntity<AccountDTO> create(CreateAccountRequest request) {
        Optional<AccountEntity> existingAccount = accountRepository.findByUserNameContainingIgnoreCase(request.getUsername());

        if (existingAccount.isPresent()) {
            throw new UserNameAlreadyExistException(request.getUsername());
        }

        var acct = new AccountEntity();
        acct.setUserName(request.getUsername());
        acct.setPassWord(encoder.encode(request.getPassword()));
        acct.setRole(request.getRole());
        if (request.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Cannot created ADMIN account");
        }
        var saveAcct = accountRepository.save(acct);
//        log.info("***Create account: {}", saveAcct);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AccountDTO(saveAcct));
    }

    @Override
    public ResponseEntity<AccountDTO> put(UUID id, UpdateAccountRequest request) {
        Optional<AccountEntity> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            AccountEntity acct = accountOptional.get();
            if (!request.getUsername().isBlank()) {
                acct.setUserName(request.getUsername());
            }
            if (!request.getPassword().isBlank()) {
                acct.setPassWord(request.getPassword());
            }
            acct.setRole(request.getRole());

            var res = accountRepository.save(acct);

            return ResponseEntity.ok(new AccountDTO(res));
        }
        throw new AccountNotFoundException();
    }

    @Override
    public ResponseEntity<PageResponse<AccountDTO>> getAll(Integer page, Integer size) {
//        log.info("***page response: {}, {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<AccountEntity> accounts = accountRepository.findAll(pageable);
        List<AccountDTO> rows = accounts.getContent().stream().map(AccountDTO::new).toList();
        var response = new PageResponse<AccountDTO>();
        response.setCount(accounts.getTotalElements());
        response.setRows(rows);
        response.setPage(page);
        response.setSize(size);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountEntity currentUser = accountRepository.findByUserNameContainingIgnoreCase(currentUsername)
                .orElseThrow(AccountNotFoundException::new);

        if (id.equals(currentUser.getAccountId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Cannot delete your own account.");
        }

        AccountEntity targetAccount = accountRepository.findById(id)
                .orElseThrow(AccountNotFoundException::new);

        if (targetAccount.getRole() == Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Cannot delete ADMIN account with id: " + targetAccount.getAccountId());
        }

        accountRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AccountDTO>> search(String name) {
        Optional<AccountEntity> accountEntityOptional = accountRepository.findByUserNameContainingIgnoreCase(name);
        if (accountEntityOptional.isPresent()) {
            return ResponseEntity.ok(accountEntityOptional.stream().map(AccountDTO::new).toList());
//            return null;
        }
        throw new AccountNotFoundException();
    }

    @Override
    public ResponseEntity<List<AccountDTO>> getAvailableAccounts() {
        log.info("***Getting available accounts");
        List<AccountEntity> allStudentAccounts = accountRepository.findByRole(Role.STUDENT);


        List<UUID> linkedAccountIds = studentRepository.findAll().stream()
                .map(student -> student.getAccountIds() != null ? student.getAccountIds().getAccountId() : null)
                .filter(accountId -> accountId != null) // Filter out null accountIds
                .collect(Collectors.toList());


        List<AccountDTO> availableAccounts = allStudentAccounts.stream()
                .filter(account -> !linkedAccountIds.contains(account.getAccountId()))
                .map(AccountDTO::new)
                .collect(Collectors.toList());

        log.info("***Available Accounts: {}", availableAccounts);

        return new ResponseEntity<>(availableAccounts, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AccountDTO>> getAvailableAccount2s() {
        log.info("***Getting available accounts");
        List<AccountEntity> allTeacherAccounts = accountRepository.findByRole(Role.TEACHER);


        List<UUID> linkedAccountIds = teacherRepository.findAll().stream()
                .map(teacher -> teacher.getAccountIds() != null ? teacher.getAccountIds().getAccountId() : null)
                .filter(accountId -> accountId != null) // Filter out null accountIds
                .collect(Collectors.toList());


        List<AccountDTO> availableAccounts = allTeacherAccounts.stream()
                .filter(account -> !linkedAccountIds.contains(account.getAccountId()))
                .map(AccountDTO::new)
                .collect(Collectors.toList());

        log.info("***Available Accounts: {}", availableAccounts);

        return new ResponseEntity<>(availableAccounts, HttpStatus.OK);
    }
}
