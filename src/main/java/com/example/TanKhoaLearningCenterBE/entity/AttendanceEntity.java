package com.example.TanKhoaLearningCenterBE.entity;

import com.example.TanKhoaLearningCenterBE.utils.classes.ClassStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "attendances")
public class AttendanceEntity extends AuditEntity {
    @Id
    @GeneratedValue
    @Column(name = "attendanceId")
    private UUID attendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    private StudentEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classId")
    private ClassEntity clazz;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ClassStatus status;
}
