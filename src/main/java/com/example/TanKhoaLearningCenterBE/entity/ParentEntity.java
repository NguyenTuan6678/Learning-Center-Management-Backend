package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Document(collection = "parents")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class ParentEntity extends AuditEntity {
    @Id
    private UUID parentId = UUID.randomUUID();

    
    private String parentName;

    
    private String parPhoneNumber;

    
    private String parEmail;

    @DBRef(lazy = true)
    
    @ToString.Exclude
    private AccountEntity accountIds;
}
