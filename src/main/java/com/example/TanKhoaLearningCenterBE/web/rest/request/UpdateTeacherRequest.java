package com.example.TanKhoaLearningCenterBE.web.rest.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateTeacherRequest {
    private String name;
    private String phoneNumber;
    private String email;
    private UUID accountId;
}
