package com.example.TanKhoaLearningCenterBE.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "classes")
public class ClassEntity extends AuditEntity {
    @Id
    @GeneratedValue
    @Column(name = "classId")
    private UUID classId;

    @Column(name = "className", nullable = false, unique = true)
    private String className;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherId", nullable = false)
    private TeacherEntity teacher;

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassStudentEntity> classStudents;

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendanceEntity> attendances;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dayId")
    private DayEntity day;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeId")
    private TimeEntity time;
}
