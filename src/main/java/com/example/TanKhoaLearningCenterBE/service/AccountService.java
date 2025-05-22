package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.AccountDTO;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateAccountRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateAccountRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    ResponseEntity<AccountDTO> create(CreateAccountRequest request);

    ResponseEntity<AccountDTO> put(UUID id, UpdateAccountRequest request);

    ResponseEntity<?> delete(UUID id);

    ResponseEntity<PageResponse<AccountDTO>> getAll(Integer page, Integer size);

    ResponseEntity<List<AccountDTO>> search(String input);

    ResponseEntity<List<AccountDTO>> getAvailableAccounts();

    ResponseEntity<List<AccountDTO>> getAvailableAccount2s();
}
