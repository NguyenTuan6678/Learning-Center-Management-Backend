package com.example.TanKhoaLearningCenterBE.web.rest;

import com.example.TanKhoaLearningCenterBE.dto.TeacherDTO;
import com.example.TanKhoaLearningCenterBE.service.TeacherService;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateTeacherRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateTeacherRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/create")
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody CreateTeacherRequest request) {
        return teacherService.create(request);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable UUID id, @RequestBody UpdateTeacherRequest request) {
        return teacherService.put(id, request);
    }

    @GetMapping("/teacherList")
    public ResponseEntity<PageResponse<TeacherDTO>> getTeachers(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return teacherService.getAll(page, size);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable UUID id) {
        return teacherService.delete(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeacherDTO>> searchTeacher(@RequestParam String name) {
        return teacherService.search(name);
    }
}
