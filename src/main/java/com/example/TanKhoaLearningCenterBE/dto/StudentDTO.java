package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.AccountEntity;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class StudentDTO {
    private UUID id;
    private String name;
    private String phoneNumber;
    private String email;
    private AccountEntity accountId;

    public StudentDTO(StudentEntity student){
        this.id = student.getStudentId();
        this.name = student.getStdName();
        this.phoneNumber = student.getStdPhoneNumber();
        this.email = student.getStdEmail();
        this.accountId = student.getAccountIds();
    }
}
