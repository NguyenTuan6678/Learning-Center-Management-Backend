package com.example.TanKhoaLearningCenterBE.web.rest;

import com.example.TanKhoaLearningCenterBE.dto.BillDetailDTO;
import com.example.TanKhoaLearningCenterBE.service.BillDetailService;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateBillDetailsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/billdetails")
@RequiredArgsConstructor
public class BillDetailsController {
    private final BillDetailService billDetailService;

    @PutMapping("/update/{id}")
    public ResponseEntity<BillDetailDTO> updateBillDetail(@PathVariable UUID id, @RequestBody UpdateBillDetailsRequest request) {
        return billDetailService.update(id, request);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BillDetailDTO> getbilldetail(@PathVariable UUID id) {
        return billDetailService.get(id);
    }
}
