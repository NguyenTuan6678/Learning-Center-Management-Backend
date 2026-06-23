package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.BillDTO;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateBillRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface BillService {
    ResponseEntity<BillDTO> create(CreateBillRequest request);

    ResponseEntity<PageResponse<BillDTO>> getAll(Integer page, Integer size);
}
