package com.example.TanKhoaLearningCenterBE.entity;

import com.example.TanKhoaLearningCenterBE.utils.bill.BillStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "bills")
public class BillEntity extends AuditEntity {
    @Id
    @GeneratedValue
    @Column(name = "billId")
    private UUID billId;

    @Column(name = "billDetail", nullable = false, unique = true)
    private String billContent;

    @Column(name = "billStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private BillStatus billStatus;

    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL)
    private BillDetailEntity billDetailEntity;

    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL)
    private PaymentEntity payment;
}
