package com.example.TanKhoaLearningCenterBE.controller.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateClassRequest {
    private String className;
    private String description;
    private UUID courseId;
    private UUID teacherId;
    private UUID dayId;
    private UUID timeId;
}
