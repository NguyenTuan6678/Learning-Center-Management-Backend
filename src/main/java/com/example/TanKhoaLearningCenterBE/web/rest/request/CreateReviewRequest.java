package com.example.TanKhoaLearningCenterBE.web.rest.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateReviewRequest {
    private UUID teacherId;
    private UUID studentId;
    private String description;
}
