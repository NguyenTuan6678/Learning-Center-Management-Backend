package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.BillDetailEntity;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class BillDetailDTO {
    private UUID billId;
    private UUID studentId;
    private UUID parentId;
    private String description;
    private Double amount;
    private String currency;
    private String paymentStatus;
    private Instant createdAt;
    private Instant updatedAt;

    public BillDetailDTO(BillDetailEntity billDetail) {
        this.billId = billDetail.getBillId();
        if (billDetail.getStudent() != null) {
            this.studentId = billDetail.getStudent().getStudentId();
        }
        if (billDetail.getParent() != null) {
            this.parentId = billDetail.getParent().getParentId();
        }
        this.description = billDetail.getDescription();
        this.amount = billDetail.getAmount();
        this.currency = billDetail.getCurrency();
        this.paymentStatus = billDetail.getPaymentStatus();
        this.createdAt = billDetail.getCreatedAt();
        this.updatedAt = billDetail.getUpdatedAt();
    }
}
