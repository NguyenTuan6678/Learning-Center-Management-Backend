package com.example.TanKhoaLearningCenterBE.web.rest.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateGradeRequest {
    private UUID studentId;
    private UUID courseId;
    private UUID teacherId;

    private Float attendanceScore;
    private Float attitudeScore;
    private Float midtermScore;
    private Float finalScore;

    private String notes;
}
