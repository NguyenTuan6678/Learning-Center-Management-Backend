package com.example.TanKhoaLearningCenterBE.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "userProfiles")
public class UserProfileEntity extends AuditEntity {
    @Id
    @GeneratedValue
    @Column(name = "userProfileId")
    private UUID userProfileId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId")
    private AccountEntity accountId;

    @Column(name = "avatar")
    private byte avatar;

    @Column(name = "gender")
    private String gender;
}
