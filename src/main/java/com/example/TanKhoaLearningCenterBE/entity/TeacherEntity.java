package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Document(collection = "teachers")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class TeacherEntity extends AuditEntity {
    @Id
    private UUID teacherId = UUID.randomUUID();

    
    private String teacherName;

    
    private String tphoneNumber;

    
    private String tEmail;

    @DBRef(lazy = true)
    
    @ToString.Exclude
    private AccountEntity accountIds;
}
