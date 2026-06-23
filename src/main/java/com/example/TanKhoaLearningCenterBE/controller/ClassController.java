package com.example.TanKhoaLearningCenterBE.controller;

import com.example.TanKhoaLearningCenterBE.dto.ClassDTO;
import com.example.TanKhoaLearningCenterBE.service.ClassService;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateClassRequest;
import com.example.TanKhoaLearningCenterBE.controller.request.UpdateClassRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/class")
public class ClassController {
    private final ClassService classService;

    @PostMapping("/create")
    public ResponseEntity<ClassDTO> createClass(@RequestBody CreateClassRequest request) {
        return classService.createClass(request);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClassDTO> updateClass(@PathVariable UUID id,@RequestBody UpdateClassRequest request) {
        return classService.updateClass(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable UUID id) {
        return classService.deleteClass(id);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ClassDTO> getClassById(@PathVariable UUID id) {
        return classService.getClassById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClassDTO>> searchClassByName(@RequestParam String name) {
        return classService.searchClassByName(name);
    }

    @GetMapping("/listAll")
    public ResponseEntity<PageResponse<ClassDTO>> getAllClasses(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return classService.getAllClasses(page, size);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ClassDTO>> getClassesByCourse(@PathVariable UUID courseId) {
        return classService.getClassesByCourse(courseId);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<ClassDTO>> getClassesByTeacher(@PathVariable UUID teacherId) {
        return classService.getClassesByTeacher(teacherId);
    }
}
