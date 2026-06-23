package com.example.TanKhoaLearningCenterBE.controller;

import com.example.TanKhoaLearningCenterBE.dto.PaymentDTO;
import com.example.TanKhoaLearningCenterBE.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class VNPAYController {
    private final VNPayService vnPayService;

    @PostMapping("/vnpay/{billId}")
    public ResponseEntity<PaymentDTO.VNPayResponse> createVNPayPayment(
            @PathVariable UUID billId,
            HttpServletRequest request) {
        return ResponseEntity.ok(vnPayService.createVNPayPayment(billId, request));
    }

    @GetMapping("/vnpay/callback")
    public void handleVNPayCallback(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam Map<String, String> callbackParams) throws java.io.IOException {
        log.info("VNPay callback invoked");
        try {
            PaymentDTO.PaymentResponse paymentResponse = vnPayService.handleVNPayCallback(request);
            
            // Redirect back to frontend student dashboard page with status & paymentId
            String frontendUrl = "http://localhost:5173/#/student?paymentStatus=success&paymentId=" + paymentResponse.getPaymentId();
            response.sendRedirect(frontendUrl);
        } catch (Exception e) {
            log.error("Unexpected error in VNPay callback", e);
            // Redirect back with failure status
            response.sendRedirect("http://localhost:5173/#/student?paymentStatus=failed");
        }
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<PaymentDTO.PaymentResponse> checkPaymentStatus(
            @PathVariable UUID paymentId) {
        log.info("Checking payment status for: {}", paymentId);
        return ResponseEntity.ok(vnPayService.getPaymentStatus(paymentId));
    }
}
