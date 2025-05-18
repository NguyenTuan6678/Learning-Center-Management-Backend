package com.example.TanKhoaLearningCenterBE.entity;

import com.example.TanKhoaLearningCenterBE.utils.bill.BillStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "payments")
public class PaymentEntity extends AuditEntity {
    @Id
    @GeneratedValue
    @Column(name = "paymentId")
    private UUID paymentId;

    @OneToOne
    @JoinColumn(name = "bill_id", unique = true)
    private BillEntity bill;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethodEntity paymentMethod;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillStatus status;
}
