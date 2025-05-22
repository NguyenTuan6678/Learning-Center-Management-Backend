package com.example.TanKhoaLearningCenterBE.web.rest;

import com.example.TanKhoaLearningCenterBE.dto.AccountDTO;
import com.example.TanKhoaLearningCenterBE.service.AccountService;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateAccountRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateAccountRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/account")     
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest account) {
        return accountService.create(account);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable UUID id) {
        log.info("🔐 Request to delete account {}", id);
        return accountService.delete(id);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable UUID id, @RequestBody UpdateAccountRequest request) {
        return accountService.put(id, request);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccountDTO>> searchAcccount(@RequestParam String name) {
        return accountService.search(name);
    }

    @GetMapping("/listacc")
    public ResponseEntity<PageResponse<AccountDTO>> getAllAccount(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                           @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return accountService.getAll(page, size);
    }

    @GetMapping("available-student")
    public ResponseEntity<List<AccountDTO>> getAvailableStudentAccounts() {
        return accountService.getAvailableAccounts();
    }

    @GetMapping("available-teacher")
    public ResponseEntity<List<AccountDTO>> getAvailableTeacherAccounts() {
        return accountService.getAvailableAccount2s();
    }
}
