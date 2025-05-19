package com.example.TanKhoaLearningCenterBE.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "class_student")
public class ClassStudentEntity extends AuditEntity{
    @Id
    @GeneratedValue
    @Column(name = "classstudentid")  // Add an ID
    private UUID classStudentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classId", nullable = false)
    private ClassEntity clazz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private StudentEntity student;
}
