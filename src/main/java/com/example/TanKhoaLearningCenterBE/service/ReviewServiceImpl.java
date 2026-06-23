package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.ReviewDTO;
import com.example.TanKhoaLearningCenterBE.entity.ReviewEntity;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import com.example.TanKhoaLearningCenterBE.entity.TeacherEntity;
import com.example.TanKhoaLearningCenterBE.repository.ReviewRepository;
import com.example.TanKhoaLearningCenterBE.repository.StudentRepository;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import com.example.TanKhoaLearningCenterBE.repository.TeacherRepository;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateReviewRequest;
import com.example.TanKhoaLearningCenterBE.controller.request.UpdateReviewRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public ResponseEntity<ReviewDTO> createReview(CreateReviewRequest request) {
        ReviewEntity review = new ReviewEntity();

        StudentEntity student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        TeacherEntity teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        review.setStudentIds(student);
        review.setTeacherIds(teacher);
        review.setDescription(request.getDescription());

        ReviewEntity saved = reviewRepository.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReviewDTO(saved));
    }

    @Override
    public ResponseEntity<ReviewDTO> updateReview(UUID id, UpdateReviewRequest request) {
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (request.getDescription() != null) {
            review.setDescription(request.getDescription());
        }

        ReviewEntity updated = reviewRepository.save(review);
        return ResponseEntity.ok(new ReviewDTO(updated));
    }

    @Override
    public ResponseEntity<?> deleteReview(UUID id) {
        if (!reviewRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        }
        reviewRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> searchReview(String name) {
        List<StudentEntity> students = studentRepository.findByStdNameContainingIgnoreCase(name);
        List<ReviewEntity> result = reviewRepository.findByStudentIdsIn(students);
        List<ReviewDTO> dtoList = result.stream().map(ReviewDTO::new).toList();
        return ResponseEntity.ok(dtoList);
    }

    @Override
    public ResponseEntity<PageResponse<ReviewDTO>> getAllReview(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewEntity> pageResult = reviewRepository.findAll(pageable);

        List<ReviewDTO> rows = pageResult.getContent().stream().map(ReviewDTO::new).toList();

        PageResponse<ReviewDTO> response = new PageResponse<>();
        response.setCount(pageResult.getTotalElements());
        response.setRows(rows);
        response.setPage(page);
        response.setSize(size);

        return ResponseEntity.ok(response);
    }
}
