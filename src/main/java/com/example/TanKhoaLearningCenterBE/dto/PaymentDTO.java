package com.example.TanKhoaLearningCenterBE.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

public class PaymentDTO {
    @Data
    @Builder
    public static class VNPayResponse {
        private String code; // "00" = success
        private String message;
        private String paymentUrl;
    }

    @Data
    @Builder
    public static class PaymentResponse {
        private UUID paymentId;
        private UUID billId;
        private Double amount;
        private String paymentMethod;
        private BillDTO bill;
        private BillDetailDTO billDetail;
        private String status;
        private Instant createdAt;
    }
}
