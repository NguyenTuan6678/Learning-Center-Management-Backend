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
import com.example.TanKhoaLearningCenterBE.web.rest.response.FileUploadResponse;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public ResponseEntity<FileUploadResponse> uploadParentsFromExcel(MultipartFile file) {
        FileUploadResponse response = new FileUploadResponse();
        if (file.isEmpty()) {
            response.setMessage("Please Upload the file!");
            response.setSuccessfulCount(0);
            return ResponseEntity.badRequest().body(response);
        }
        try {
            List<CreateParentRequest> parents = processExcelFile(file);
            if (parents.isEmpty()) { // Kiểm tra danh sách parents có rỗng không
                response.setMessage("No valid parents found in the Excel file.");
                response.setSuccessfulCount(0);
                return ResponseEntity.badRequest().body(response);
            }
            saveParents(parents);
            response.setMessage("Parents uploaded successfully!");
            response.setSuccessfulCount(parents.size());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.setMessage("Failed to upload parents: " + e.getMessage());
            response.setSuccessfulCount(0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (IllegalArgumentException e) { // Bắt lỗi dữ liệu không hợp lệ
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
            return ""; // Trả về "" thay vì null
        }
        cell.setCellType(CellType.STRING);
        String value = cell.getStringCellValue().trim();
        return value;
    }

    @Override
    public List<CreateParentRequest> processExcelFile(MultipartFile file) throws IOException {
        List<CreateParentRequest> parents = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                CreateParentRequest parentRequest = new CreateParentRequest();

                String name = getStringCellValue(row.getCell(0));
                String phone = getStringCellValue(row.getCell(1));
                String email = getStringCellValue(row.getCell(2));

                // Validate dữ liệu trước khi tạo đối tượng ParentRequest
                if (isValidParentData(name, phone, email)) {
                    parentRequest.setName(name);
                    parentRequest.setPhoneNumber(phone);
                    parentRequest.setEmail(email);
                    parents.add(parentRequest);
                } else {
                    throw new IllegalArgumentException("Invalid data in row " + (row.getRowNum() + 1) +
                            ": Name='" + name + "', Phone='" + phone + "', Email='" + email + "'");
                }
            }
        }
        return parents;
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

    private boolean isValidParentData(String name, String phone, String email) {
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
    public void saveParents(List<CreateParentRequest> parentRequests) {
        List<ParentEntity> parentEntities = parentRequests.stream()
                .map(this::converToEntity)
                .collect(Collectors.toList());
        parentRepository.saveAll(parentEntities);
    }

    private ParentEntity converToEntity(CreateParentRequest request) {
        ParentEntity parentEntity = new ParentEntity();
        parentEntity.setParentName(request.getName());
        parentEntity.setParPhoneNumber(request.getPhoneNumber());
        parentEntity.setParEmail(request.getEmail());
        return parentEntity;
    }
}
