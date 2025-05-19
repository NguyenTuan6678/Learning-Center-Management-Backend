package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.GradeDTO;
import com.example.TanKhoaLearningCenterBE.dto.ReviewDTO;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateGradeRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateReviewRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateGradeRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateReviewRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    ResponseEntity<ReviewDTO> createReview(CreateReviewRequest request);

    ResponseEntity<ReviewDTO> updateReview(UUID id, UpdateReviewRequest request);

    ResponseEntity<?> deleteReview(UUID id);

    ResponseEntity<List<ReviewDTO>> searchReview(String name);

    ResponseEntity<PageResponse<ReviewDTO>> getAllReview(Integer page, Integer size);
}
