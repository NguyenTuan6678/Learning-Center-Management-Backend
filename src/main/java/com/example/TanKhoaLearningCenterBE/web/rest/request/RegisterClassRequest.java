package com.example.TanKhoaLearningCenterBE.web.rest.request;

import lombok.Data;

import java.util.UUID;

@Data
public class RegisterClassRequest {
    private UUID studentId;
    private UUID classId;
}
