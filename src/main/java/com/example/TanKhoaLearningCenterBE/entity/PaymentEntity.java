package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.example.TanKhoaLearningCenterBE.utils.bill.BillStatus;

import lombok.Data;

import java.util.UUID;

@Document(collection = "payments")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class PaymentEntity extends AuditEntity {
    @Id
    private UUID paymentId = UUID.randomUUID();

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private BillEntity bill;

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private PaymentMethodEntity paymentMethod;

    
    private Double amount;

    
    
    private BillStatus status;
}
