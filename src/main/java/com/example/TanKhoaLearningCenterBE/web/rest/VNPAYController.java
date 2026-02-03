package com.example.TanKhoaLearningCenterBE.web.rest;

import com.example.TanKhoaLearningCenterBE.dto.PaymentDTO;
import com.example.TanKhoaLearningCenterBE.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<PaymentDTO.PaymentResponse> handleVNPayCallback(
            HttpServletRequest request,
            @RequestParam Map<String, String> callbackParams) {
        log.info("VNPay callback invoked");
        try {
            PaymentDTO.PaymentResponse response = vnPayService.handleVNPayCallback(request);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            log.error("VNPay callback processing failed: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            log.error("Unexpected error in VNPay callback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<PaymentDTO.PaymentResponse> checkPaymentStatus(
            @PathVariable UUID paymentId) {
        log.info("Checking payment status for: {}", paymentId);
        return ResponseEntity.ok(vnPayService.getPaymentStatus(paymentId));
    }
}
