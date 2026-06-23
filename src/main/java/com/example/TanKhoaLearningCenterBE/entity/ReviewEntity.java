package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;

import java.util.UUID;

@Document(collection = "reviews")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class ReviewEntity extends AuditEntity {
    @Id
    private UUID reviewId = UUID.randomUUID();

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private TeacherEntity teacherIds;

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private StudentEntity studentIds;

    
    private String description;
}
