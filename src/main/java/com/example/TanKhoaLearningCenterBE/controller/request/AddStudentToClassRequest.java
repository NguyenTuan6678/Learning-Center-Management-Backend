package com.example.TanKhoaLearningCenterBE.controller.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AddStudentToClassRequest {
    private UUID classId;
    private UUID studentId;
}
