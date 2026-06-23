package com.example.TanKhoaLearningCenterBE.controller;

import com.example.TanKhoaLearningCenterBE.dto.BillDTO;
import com.example.TanKhoaLearningCenterBE.dto.PaymentDTO;
import com.example.TanKhoaLearningCenterBE.service.BillService;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateBillRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/bill")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @PostMapping("/create")
    public ResponseEntity<BillDTO> createBill(@RequestBody CreateBillRequest request) {
        return billService.create(request);
    }

    @GetMapping("/listAll")
    public ResponseEntity<PageResponse<BillDTO>> gettAllBill(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                             @RequestParam(value = "page", required = false, defaultValue = "10") Integer size) {
        return billService.getAll(page, size);
    }
}
