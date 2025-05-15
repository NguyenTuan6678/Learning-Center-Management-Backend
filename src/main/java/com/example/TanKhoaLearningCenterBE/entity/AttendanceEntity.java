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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "studentId")
    private StudentEntity student;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "classId")
    private ClassEntity clazz;

    @Column(name = "status")
    private ClassStatus status;
}
