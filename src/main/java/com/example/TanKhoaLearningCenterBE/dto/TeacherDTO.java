package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.AccountEntity;
import com.example.TanKhoaLearningCenterBE.entity.TeacherEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class TeacherDTO {
    private UUID id;
    private String name;
    private String phoneNumber;
    private String email;
    private AccountEntity accountId;

    public TeacherDTO(TeacherEntity teacher){
        this.id = teacher.getTeacherId();
        this.name = teacher.getTeacherName();
        this.phoneNumber = teacher.getTphoneNumber();
        this.email = teacher.getTEmail();
        this.accountId = teacher.getAccountIds();
    }
}
