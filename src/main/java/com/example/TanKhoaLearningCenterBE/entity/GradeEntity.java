package com.example.TanKhoaLearningCenterBE.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@Table(name = "grades")
public class GradeEntity extends AuditEntity{
    @Id
    @GeneratedValue
    @Column(name = "gradeid")
    private UUID gradeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private StudentEntity student;

    @Column(name = "studentName", nullable = false)
    private String studentName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private CourseEntity course;

    @Column(name = "courseName", nullable = false)
    private String courseName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherId", nullable = false)
    private TeacherEntity teacher;

    @Column(name = "teacherName", nullable = false)
    private String teacherName; // Lưu tên giáo viên chấm điểm

    @Column(name = "attendanceScore")
    private Float attendanceScore;

    @Column(name = "attitudeScore")
    private Float attitudeScore;

    @Column(name = "midtermScore")
    private Float midtermScore;

    @Column(name = "finalScore")
    private Float finalScore;

    @Column(name = "notes")
    private String notes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GradeEntity)) return false;
        GradeEntity that = (GradeEntity) o;
        return Objects.equals(student, that.student) && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course);
    }
}
