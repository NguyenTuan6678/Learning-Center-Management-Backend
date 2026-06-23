package com.example.TanKhoaLearningCenterBE.controller.request;

import lombok.Data;

@Data
public class CreateCourseRequest {
    private String courseName;
    private String description;
}
