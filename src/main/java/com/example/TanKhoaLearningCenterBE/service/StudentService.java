package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.StudentDTO;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateStudentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateStudentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    ResponseEntity<StudentDTO> create(CreateStudentRequest request);

    ResponseEntity<StudentDTO> put(UUID id, UpdateStudentRequest request);

    ResponseEntity<?> delete(UUID id);

    ResponseEntity<StudentDTO> get(UUID id);

    ResponseEntity<PageResponse<StudentDTO>> getAll(Integer page, Integer size);

    ResponseEntity<List<StudentDTO>> search(String name);
}
