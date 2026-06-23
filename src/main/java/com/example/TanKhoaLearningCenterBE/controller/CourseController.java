package com.example.TanKhoaLearningCenterBE.controller;

import com.example.TanKhoaLearningCenterBE.dto.CourseDTO;
import com.example.TanKhoaLearningCenterBE.service.CourseService;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateCourseRequest;
import com.example.TanKhoaLearningCenterBE.controller.request.UpdateCourseRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<CourseDTO> createACourse(@RequestBody CreateCourseRequest request) {
        return courseService.createCourse(request);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CourseDTO> updateACourse(@PathVariable UUID id, @RequestBody UpdateCourseRequest request) {
        return courseService.updateCourse(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteACourse(@PathVariable UUID id) {
        return courseService.deleteCourse(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDTO>> searchACourse(@RequestParam String name) {
        return courseService.searchCourse(name);
    }

    @GetMapping("/courseList")
    public ResponseEntity<PageResponse<CourseDTO>> getAllCourseI(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page, @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return courseService.getAllCourse(page, size);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<CourseDTO>> getAllCourseBy() {
        return courseService.getCourseById();
    }
}
