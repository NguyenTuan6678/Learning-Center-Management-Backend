package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.ClassDTO;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateClassRequest;
import com.example.TanKhoaLearningCenterBE.controller.request.UpdateClassRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ClassService {
    ResponseEntity<ClassDTO> createClass(CreateClassRequest request);

    ResponseEntity<ClassDTO> updateClass(UUID id, UpdateClassRequest request);

    ResponseEntity<?> deleteClass(UUID id);

    ResponseEntity<ClassDTO> getClassById(UUID id);

    ResponseEntity<List<ClassDTO>> searchClassByName(String name);

    ResponseEntity<PageResponse<ClassDTO>> getAllClasses(Integer page, Integer size);

    ResponseEntity<List<ClassDTO>> getClassesByCourse(UUID courseId);

    ResponseEntity<List<ClassDTO>> getClassesByTeacher(UUID teacherId);
}
