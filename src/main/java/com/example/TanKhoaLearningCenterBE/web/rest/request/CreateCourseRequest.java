package com.example.TanKhoaLearningCenterBE.web.rest.request;

import lombok.Data;

@Data
public class CreateCourseRequest {
    private String courseName;
    private String description;
}
