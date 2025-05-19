package com.example.TanKhoaLearningCenterBE.web.rest.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AddStudentToClassRequest {
    private UUID classId;
    private UUID studentId;
}
