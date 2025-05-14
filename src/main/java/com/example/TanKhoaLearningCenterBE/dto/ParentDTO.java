package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.AccountEntity;
import com.example.TanKhoaLearningCenterBE.entity.ParentEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class ParentDTO {
    private UUID id;
    private String name;
    private String phoneNumber;
    private String email;
    private AccountEntity accountId;

    public ParentDTO(ParentEntity parent){
        this.id = parent.getParentId();
        this.name = parent.getParentName();
        this.phoneNumber = parent.getParPhoneNumber();
        this.email = parent.getParEmail();
        this.accountId = parent.getAccountIds();
    }
}
