package com.example.TanKhoaLearningCenterBE.web.rest;

import com.example.TanKhoaLearningCenterBE.dto.StudentDTO;
import com.example.TanKhoaLearningCenterBE.service.StudentService;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateStudentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateStudentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.FileUploadResponse;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/create")
    public ResponseEntity<StudentDTO> createStudent(@RequestBody CreateStudentRequest student) {
        return studentService.create(student);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable UUID id, @RequestBody UpdateStudentRequest student) {
        return studentService.put(id, student);
    }

    @GetMapping("/studentList")
    public ResponseEntity<PageResponse<StudentDTO>> getStudents(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page, @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return studentService.getAll(page, size);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable UUID id) {
        return studentService.delete(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentDTO>> searchStudent(@RequestParam String name) {
        return studentService.search(name);
    }

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadStudentsFromExcel(@RequestParam("file")MultipartFile file) {
        return studentService.uploadStudentsFromExcel(file);
    }
}
