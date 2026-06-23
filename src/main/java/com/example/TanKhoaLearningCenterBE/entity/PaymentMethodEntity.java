package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;

import java.util.UUID;

@Document(collection = "paymentMethod")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class PaymentMethodEntity extends AuditEntity {
    @Id
    private UUID payMId = UUID.randomUUID();

    
    private String payMethod;
}
