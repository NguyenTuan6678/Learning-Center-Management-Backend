package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.GradeDTO;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateGradeRequest;
import com.example.TanKhoaLearningCenterBE.controller.request.UpdateGradeRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface GradeService {
    ResponseEntity<GradeDTO> createGrade(CreateGradeRequest request);

    ResponseEntity<GradeDTO> updateGrade(UUID id, UpdateGradeRequest request);

    ResponseEntity<?> deleteGrade(UUID id);

    ResponseEntity<List<GradeDTO>> searchGrade(String name);

    ResponseEntity<PageResponse<GradeDTO>> getAllGrade(Integer page, Integer size);
}
