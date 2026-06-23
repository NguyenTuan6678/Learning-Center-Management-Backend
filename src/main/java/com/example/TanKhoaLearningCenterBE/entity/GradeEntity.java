package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Document(collection = "grades")
@Data
public class GradeEntity extends AuditEntity{
    @Id
    private UUID gradeId = UUID.randomUUID();

    @DBRef(lazy = true)
    
    private StudentEntity student;

    
    private String studentName;

    @DBRef(lazy = true)
    
    private CourseEntity course;

    
    private String courseName;

    @DBRef(lazy = true)
    
    private TeacherEntity teacher;

    
    private String teacherName; // Lưu tên giáo viên chấm điểm

    
    private Float attendanceScore;

    
    private Float attitudeScore;

    
    private Float midtermScore;

    
    private Float finalScore;

    
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
