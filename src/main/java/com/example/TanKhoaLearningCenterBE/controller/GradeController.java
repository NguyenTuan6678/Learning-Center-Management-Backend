package com.example.TanKhoaLearningCenterBE.controller;

import com.example.TanKhoaLearningCenterBE.dto.GradeDTO;
import com.example.TanKhoaLearningCenterBE.service.GradeService;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateGradeRequest;
import com.example.TanKhoaLearningCenterBE.controller.request.UpdateGradeRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/grade")
public class GradeController {
    private final GradeService gradeService;

    @PostMapping("/create")
    public ResponseEntity<GradeDTO> createAGrade(@RequestBody CreateGradeRequest request) {
        return gradeService.createGrade(request);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GradeDTO> updateAGrade(@PathVariable UUID id, @RequestBody UpdateGradeRequest request) {
        return gradeService.updateGrade(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAGreade(@PathVariable UUID id) {
        return gradeService.deleteGrade(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GradeDTO>> searchACourse(@RequestParam String name) {
        return gradeService.searchGrade(name);
    }

    @GetMapping("/listAll")
    public ResponseEntity<PageResponse<GradeDTO>> getAllCourseI(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page, @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return gradeService.getAllGrade(page, size);
    }
}
