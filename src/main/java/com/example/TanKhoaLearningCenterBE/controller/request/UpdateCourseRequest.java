package com.example.TanKhoaLearningCenterBE.controller.request;

import lombok.Data;

@Data
public class UpdateCourseRequest {
    private String courseName;
    private String description;
}
