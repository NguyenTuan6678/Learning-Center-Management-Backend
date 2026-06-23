package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.CourseDTO;
import com.example.TanKhoaLearningCenterBE.entity.CourseEntity;
import com.example.TanKhoaLearningCenterBE.repository.CourseRepository;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateCourseRequest;
import com.example.TanKhoaLearningCenterBE.controller.request.UpdateCourseRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    @Override
    public ResponseEntity<CourseDTO> createCourse(CreateCourseRequest request) {
        var course = new CourseEntity();
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());

        CourseEntity savedCourse = courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CourseDTO(savedCourse));
    }

    @Override
    public ResponseEntity<CourseDTO> updateCourse(UUID id, UpdateCourseRequest request) {
        Optional<CourseEntity> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            CourseEntity course = optionalCourse.get();
            if (request.getCourseName() != null && !request.getCourseName().isBlank()) {
                course.setCourseName(request.getCourseName());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                course.setDescription(request.getDescription());
            }
            CourseEntity updatedCourse = courseRepository.save(course);
            return ResponseEntity.ok(new CourseDTO(updatedCourse));
        }
        throw new RuntimeException("Course not found");
    }

    @Override
    public ResponseEntity<?> deleteCourse(UUID id) {
        Optional<CourseEntity> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            courseRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new RuntimeException("Course not found");
    }

    @Override
    public ResponseEntity<List<CourseDTO>> searchCourse(String name) {
        List<CourseEntity> courses = courseRepository.findByCourseNameContainingIgnoreCase(name);
        if (!courses.isEmpty()) {
            List<CourseDTO> result = courses.stream().map(CourseDTO::new).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        }
        throw new RuntimeException("Course not found");
    }

    @Override
    public ResponseEntity<PageResponse<CourseDTO>> getAllCourse(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseEntity> coursePage = courseRepository.findAll(pageable);
        List<CourseDTO> rows = coursePage.getContent().stream().map(CourseDTO::new).collect(Collectors.toList());

        PageResponse<CourseDTO> response = new PageResponse<>();
        response.setPage(page);
        response.setSize(size);
        response.setCount(coursePage.getTotalElements());
        response.setRows(rows);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<CourseDTO>> getCourseById() {
        List<CourseEntity> courses =  courseRepository.findAll();
        List<CourseDTO> courseDTOS = courses.stream().map(CourseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(courseDTOS);
    }
}
