package com.example.TanKhoaLearningCenterBE.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


import lombok.Data;

import java.util.UUID;

@Document(collection = "userProfiles")
@Data
@lombok.EqualsAndHashCode(callSuper = true)
public class UserProfileEntity extends AuditEntity {
    @Id
    private UUID userProfileId = UUID.randomUUID();

    @DBRef(lazy = true)
    
    @lombok.ToString.Exclude
    private AccountEntity accountId;

    
    private byte avatar;

    
    private String gender;
}
