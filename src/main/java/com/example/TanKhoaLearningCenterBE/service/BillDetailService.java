package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.BillDetailDTO;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateBillDetailsRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface BillDetailService {
    ResponseEntity<BillDetailDTO> update(UUID id, UpdateBillDetailsRequest request);

    ResponseEntity<BillDetailDTO> get(UUID id);

    ResponseEntity<BillDetailDTO> getBillDetailForStudent(UUID billId, UUID studentId);

    ResponseEntity<List<BillDetailDTO>> getAllBillDetailsByStudent(UUID studentId);

}
