package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.TeacherDTO;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateTeacherRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateTeacherRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.FileUploadResponse;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface TeacherService {
    ResponseEntity<TeacherDTO> create(CreateTeacherRequest request);

    ResponseEntity<TeacherDTO> put(UUID id, UpdateTeacherRequest request);

    ResponseEntity<?> delete(UUID id);

    ResponseEntity<TeacherDTO> get(UUID id);

    ResponseEntity<PageResponse<TeacherDTO>> getAll(Integer page, Integer size);

    ResponseEntity<List<TeacherDTO>> search(String name);

    ResponseEntity<FileUploadResponse> uploadTeachersFromExcel(MultipartFile file);

    List<CreateTeacherRequest> processExcelFile(MultipartFile file) throws IOException;

    void saveTeachers(List<CreateTeacherRequest> teacherRequests);

    ResponseEntity<List<TeacherDTO>> getAllTeachers();
}