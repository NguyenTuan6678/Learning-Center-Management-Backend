package com.example.TanKhoaLearningCenterBE.web.rest;

import com.example.TanKhoaLearningCenterBE.dto.ClassStudentDTO;
import com.example.TanKhoaLearningCenterBE.service.ClassStudentService;
import com.example.TanKhoaLearningCenterBE.web.rest.request.AddStudentToClassRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/classstudents")
@RequiredArgsConstructor
public class ClassStudentController {
    private final ClassStudentService classStudentService;

    @PostMapping("/create")
    public ResponseEntity<ClassStudentDTO> addStudentToClass(@RequestBody AddStudentToClassRequest request) {
        return classStudentService.addStudentToClass(request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> removeStudentFromClass(@PathVariable UUID id) {
        return classStudentService.removeStudentFromClass(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassStudentDTO> getClassStudentById(@PathVariable UUID id) {
        return classStudentService.getClassStudentById(id);
    }

    @GetMapping("/listAll")
    public ResponseEntity<PageResponse<ClassStudentDTO>> getAllClassStudents(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return classStudentService.getAllClassStudents(page, size);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<ClassStudentDTO>> getClassStudentsByClass(@PathVariable UUID classId) {
        return classStudentService.getClassStudentsByClass(classId);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ClassStudentDTO>> getClassStudentsByStudent(@PathVariable UUID studentId) {
        return classStudentService.getClassStudentsByStudent(studentId);
    }
}
