package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.AccountEntity;
import com.example.TanKhoaLearningCenterBE.entity.TeacherEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class TeacherDTO {
    private UUID teacherId;
    private String teacherName;
    private String phoneNumber;
    private String email;
    private AccountEntity accountId;

    public TeacherDTO(TeacherEntity teacher){
        this.teacherId = teacher.getTeacherId();
        this.teacherName = teacher.getTeacherName();
        this.phoneNumber = teacher.getTphoneNumber();
        this.email = teacher.getTEmail();
        this.accountId = teacher.getAccountIds();
    }
}
