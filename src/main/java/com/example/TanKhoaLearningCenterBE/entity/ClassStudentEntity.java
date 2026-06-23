package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;

import java.util.UUID;

@Document(collection = "class_student")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class ClassStudentEntity extends AuditEntity{
    @Id
    private UUID classStudentId = UUID.randomUUID();

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private ClassEntity clazz;

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private StudentEntity student;
}
