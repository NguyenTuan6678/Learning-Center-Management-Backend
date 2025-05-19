package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.ReviewEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class ReviewDTO {
    private UUID reviewId;
    private UUID teacherId;
    private UUID studentId;
    private String description;

    private String teacherName;
    private String studentName;

    public ReviewDTO(ReviewEntity review) {
        this.reviewId = review.getReviewId();
        this.teacherId = review.getTeacherIds().getTeacherId();
        this.studentId = review.getStudentIds().getStudentId();
        this.description = review.getDescription();
        this.teacherName = review.getTeacherIds().getTeacherName();
        this.studentName = review.getStudentIds().getStdName();
    }
}
