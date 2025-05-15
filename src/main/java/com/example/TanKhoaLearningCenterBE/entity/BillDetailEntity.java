package com.example.TanKhoaLearningCenterBE.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "billdetails")
public class BillDetailEntity extends AuditEntity{
    @Id
    @GeneratedValue
    @Column(name = "bill_Id")
    private UUID billId;

    @OneToOne
    @JoinColumn(name = "bill_id")
    @MapsId
    private BillEntity bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ParentEntity parent;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;
}
