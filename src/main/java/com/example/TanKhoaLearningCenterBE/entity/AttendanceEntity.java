package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.example.TanKhoaLearningCenterBE.utils.classes.ClassStatus;

import lombok.Data;

import java.util.UUID;

@Document(collection = "attendances")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class AttendanceEntity extends AuditEntity {
    @Id
    private UUID attendId = UUID.randomUUID();

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private StudentEntity student;

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private ClassEntity clazz;

    
    
    private ClassStatus status;
}
