package com.example.TanKhoaLearningCenterBE.web.rest.request;

import lombok.Data;

@Data
public class UpdateCourseRequest {
    private String courseName;
    private String description;
}
