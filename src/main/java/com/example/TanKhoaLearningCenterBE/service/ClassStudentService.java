package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.ClassStudentDTO;
import com.example.TanKhoaLearningCenterBE.web.rest.request.AddStudentToClassRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ClassStudentService {
    ResponseEntity<ClassStudentDTO> addStudentToClass(AddStudentToClassRequest request);

    ResponseEntity<?> removeStudentFromClass(UUID classStudentId);

    ResponseEntity<ClassStudentDTO> getClassStudentById(UUID id);

    ResponseEntity<PageResponse<ClassStudentDTO>> getAllClassStudents(Integer page, Integer size);

    ResponseEntity<List<ClassStudentDTO>> getClassStudentsByClass(UUID classId);

    ResponseEntity<List<ClassStudentDTO>> getClassStudentsByStudent(UUID studentId);
}
