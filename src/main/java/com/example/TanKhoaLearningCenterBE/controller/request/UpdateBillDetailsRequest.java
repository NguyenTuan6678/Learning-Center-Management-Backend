package com.example.TanKhoaLearningCenterBE.controller.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateBillDetailsRequest {
    private String description;
    private Double amount;
    private String currency;
    private String paymentStatus;
    private UUID studentId;
    private UUID parentId;
}
