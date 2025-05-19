package com.example.TanKhoaLearningCenterBE.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "courses")
public class CourseEntity extends AuditEntity {
    @Id
    @GeneratedValue
    @Column(name = "courseId")
    private UUID courseId;

    @Column(name = "courseName", nullable = false, unique = true)
    private String courseName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassEntity> classes;
}
