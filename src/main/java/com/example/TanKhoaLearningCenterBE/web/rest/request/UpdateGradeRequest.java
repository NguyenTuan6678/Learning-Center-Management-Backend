package com.example.TanKhoaLearningCenterBE.web.rest.request;

import lombok.Data;

@Data
public class UpdateGradeRequest {
    private Float attendanceScore;
    private Float attitudeScore;
    private Float midtermScore;
    private Float finalScore;

    private String notes;
}

