package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;

import java.util.List;
import java.util.UUID;

@Document(collection = "courses")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class CourseEntity extends AuditEntity {
    @Id
    private UUID courseId = UUID.randomUUID();

    
    private String courseName;

    
    private String description;

    @DBRef(lazy = true)
    private List<ClassEntity> classes;
}
