package com.example.TanKhoaLearningCenterBE.web.rest;

import com.example.TanKhoaLearningCenterBE.dto.ReviewDTO;
import com.example.TanKhoaLearningCenterBE.service.ReviewService;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateReviewRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateReviewRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<ReviewDTO> createAReview(@RequestBody CreateReviewRequest request) {
        return reviewService.createReview(request);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReviewDTO> updateAReview(@PathVariable UUID id, @RequestBody UpdateReviewRequest request) {
        return reviewService.updateReview(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAReview(@PathVariable UUID id) {
        return reviewService.deleteReview(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReviewDTO>> searchAReview(@RequestParam String name) {
        return reviewService.searchReview(name);
    }

    @GetMapping("/listAll")
    public ResponseEntity<PageResponse<ReviewDTO>> getAllReviewI(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page, @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return reviewService.getAllReview(page, size);
    }
}
