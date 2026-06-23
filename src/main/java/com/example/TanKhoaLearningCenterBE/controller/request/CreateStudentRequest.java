package com.example.TanKhoaLearningCenterBE.controller.request;

import lombok.Data;

@Data
public class CreateStudentRequest {
    private String name;
    private String phoneNumber;
    private String email;
}
