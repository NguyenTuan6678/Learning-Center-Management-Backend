package com.example.TanKhoaLearningCenterBE.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.UUID;

@Entity
@Data
@Table(name = "students")
public class StudentEntity extends AuditEntity {
    @Id
    @GeneratedValue
    @Column(name = "studentId")
    private UUID studentId;

    @NotNull(message = "Name is required")
    @Column(name = "stdName")
    private String stdName;

    @NotNull(message = "Phone number is required")
    @Column(name = "stdPhoneNumber", unique = true)
    private String stdPhoneNumber;

    @Column(name = "stdEmail")
    private String stdEmail;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentId")
    private ParentEntity parentIds;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId")
    @ToString.Exclude
    private AccountEntity accountIds;
}