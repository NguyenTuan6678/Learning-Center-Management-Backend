package com.example.TanKhoaLearningCenterBE.web.rest.request;

import lombok.Data;

@Data
public class CreateTeacherRequest {
    private String name;
    private String phoneNumber;
    private String email;
}
