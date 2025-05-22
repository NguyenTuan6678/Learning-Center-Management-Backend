package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.CourseDTO;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateCourseRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateCourseRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    ResponseEntity<CourseDTO> createCourse(CreateCourseRequest request);

    ResponseEntity<CourseDTO> updateCourse(UUID id, UpdateCourseRequest request);

    ResponseEntity<?> deleteCourse(UUID id);

    ResponseEntity<List<CourseDTO>> searchCourse(String name);

    ResponseEntity<PageResponse<CourseDTO>> getAllCourse(Integer page, Integer size);

    ResponseEntity<List<CourseDTO>> getCourseById();
}
