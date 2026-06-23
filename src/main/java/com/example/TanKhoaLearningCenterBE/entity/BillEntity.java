package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.example.TanKhoaLearningCenterBE.utils.bill.BillStatus;

import lombok.Data;

import java.util.UUID;

@Document(collection = "bills")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class BillEntity extends AuditEntity {
    @Id
    private UUID billId = UUID.randomUUID();

    
    private String billContent;

    
    
    private BillStatus billStatus;

    @DBRef(lazy = true)
    @lombok.ToString.Exclude
    private BillDetailEntity billDetailEntity;

    @DBRef(lazy = true)
    @lombok.ToString.Exclude
    private PaymentEntity payment;
}
