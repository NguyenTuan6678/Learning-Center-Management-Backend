package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.ParentDTO;
import com.example.TanKhoaLearningCenterBE.entity.AccountEntity;
import com.example.TanKhoaLearningCenterBE.entity.BillDetailEntity;
import com.example.TanKhoaLearningCenterBE.entity.ParentEntity;
import com.example.TanKhoaLearningCenterBE.exception.AccountAlreadyAssignedException;
import com.example.TanKhoaLearningCenterBE.exception.AccountNotFoundException;
import com.example.TanKhoaLearningCenterBE.exception.InvalidAccountRoleException;
import com.example.TanKhoaLearningCenterBE.exception.ParentNotFoundException;
import com.example.TanKhoaLearningCenterBE.repository.AccountRepository;
import com.example.TanKhoaLearningCenterBE.repository.BillDetailRepository;
import com.example.TanKhoaLearningCenterBE.repository.ParentRepository;
import com.example.TanKhoaLearningCenterBE.utils.user.Role;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateParentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateParentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;
    private final BillDetailRepository billDetailRepository;
    private final AccountRepository accountRepository;

    @Override
    public ResponseEntity<ParentDTO> create(CreateParentRequest request) {
        var par = new ParentEntity();

        par.setParentName(request.getName());
        par.setParPhoneNumber(request.getPhoneNumber());
        par.setParEmail(request.getEmail());
        var savePar = parentRepository.save(par);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ParentDTO(savePar));
    }

    @Override
    public ResponseEntity<ParentDTO> put(UUID id, UpdateParentRequest request) {
        Optional<ParentEntity> optionalParent = parentRepository.findById(id);
        if (optionalParent.isPresent()) {
            ParentEntity part = optionalParent.get();
            if (!request.getName().isBlank()) {
                part.setParentName(request.getName());
            }

            if (!request.getPhoneNumber().isBlank()) {
                part.setParPhoneNumber(request.getPhoneNumber());
            }

            if (!request.getEmail().isBlank()) {
                part.setParEmail(request.getEmail());
            }

            if (request.getAccountId() != null) {
                Optional<AccountEntity> optionalAccount = accountRepository.findById(request.getAccountId());
                if (optionalAccount.isPresent()) {
                    AccountEntity account = optionalAccount.get();
                    if (account.getRole() == Role.PARENT) {
                        if (isAccountAssigned(account.getAccountId())) {
                            throw new AccountAlreadyAssignedException();
                        }
                        part.setAccountIds(account);
                    } else {
                        throw new InvalidAccountRoleException();
                    }
                } else {
                    throw new AccountNotFoundException();
                }
            }

            var res = parentRepository.save(part);

            return ResponseEntity.ok(new ParentDTO(res));
        }
        throw new ParentNotFoundException();
    }

    @Override
    public ResponseEntity<PageResponse<ParentDTO>> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ParentEntity> parents = parentRepository.findAll(pageable);
        List<ParentDTO> rows = parents.getContent().stream().map(ParentDTO::new).toList();

        var response = new PageResponse<ParentDTO>();
        response.setCount(parents.getTotalElements());
        response.setRows(rows);
        response.setPage(page);
        response.setSize(size);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ParentDTO> get(UUID id) {
        Optional<ParentEntity> optionalParent = parentRepository.findById(id);
        if (optionalParent.isPresent()) {
            return ResponseEntity.ok(new ParentDTO(optionalParent.get()));
        }
        throw new ParentNotFoundException();
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        Optional<ParentEntity> optionalParent = parentRepository.findById(id);
        List<BillDetailEntity> details = billDetailRepository.findAllByParent_ParentId(id);
        for (BillDetailEntity detail : details) {
            detail.setParent(null);
        }
        billDetailRepository.saveAll(details);
        if (optionalParent.isPresent()) {
            parentRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new ParentNotFoundException();
    }

    @Override
    public ResponseEntity<List<ParentDTO>> search(String name) {
        List<ParentEntity> parentEntityList = parentRepository.findByParentNameContainingIgnoreCase(name);
        if (!parentEntityList.isEmpty()) {
            return ResponseEntity.ok(parentEntityList.stream().map(ParentDTO::new).toList());
        }
        throw new ParentNotFoundException();
    }

    private boolean isAccountAssigned(UUID accountId) {

        return parentRepository.existsByAccountIds_AccountId(accountId);
    }
}
