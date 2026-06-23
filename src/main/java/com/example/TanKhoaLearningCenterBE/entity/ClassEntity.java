package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;

import java.util.List;
import java.util.UUID;

@Document(collection = "classes")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class ClassEntity extends AuditEntity {
    @Id
    private UUID classId = UUID.randomUUID();

    
    private String className;

    
    private String description;

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private CourseEntity course;

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private TeacherEntity teacher;

    @DBRef(lazy = true)
    @lombok.ToString.Exclude
    private List<ClassStudentEntity> classStudents;

    @DBRef(lazy = true)
    @lombok.ToString.Exclude
    private List<AttendanceEntity> attendances;

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private DayEntity day;

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private TimeEntity time;
}
