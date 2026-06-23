package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document(collection = "students")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class StudentEntity extends AuditEntity {
    @Id
    private UUID studentId = UUID.randomUUID();

    @NotNull(message = "Name is required")
    
    private String stdName;

    @NotNull(message = "Phone number is required")
    
    private String stdPhoneNumber;

    
    private String stdEmail;

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private ParentEntity parentIds;

    @DBRef(lazy = true)
    
    @ToString.Exclude
    private AccountEntity accountIds;

    @DBRef(lazy = true)
    @lombok.ToString.Exclude
    private List<ClassStudentEntity> classStudents;
}