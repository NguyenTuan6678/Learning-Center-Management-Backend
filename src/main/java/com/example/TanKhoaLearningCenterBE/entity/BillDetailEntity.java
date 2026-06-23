package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;

import java.util.UUID;

@Document(collection = "billdetails")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class BillDetailEntity extends AuditEntity{
    @Id
    private UUID billId;

    @DBRef(lazy = true)
    
    
    @lombok.ToString.Exclude
    private BillEntity bill;

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private StudentEntity student;

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private ParentEntity parent;

    
    private String description;

    
    private Double amount;

    
    private String currency;

    
    private String paymentStatus;

    public void setBill(BillEntity bill) {
        this.bill = bill;
        if (bill != null) {
            this.billId = bill.getBillId();
        }
    }
}
