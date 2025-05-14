package com.example.TanKhoaLearningCenterBE.web.rest;

import com.example.TanKhoaLearningCenterBE.dto.ParentDTO;
import com.example.TanKhoaLearningCenterBE.service.ParentService;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateParentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateParentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
public class ParentController {
    private final ParentService parentService;

    @PostMapping("/create")
    public ResponseEntity<ParentDTO> createParent(@RequestBody CreateParentRequest request) {
        return parentService.create(request);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ParentDTO> updateTeacher(@PathVariable UUID id, @RequestBody UpdateParentRequest request) {
        return parentService.put(id, request);
    }

    @GetMapping("/listAll")
    public ResponseEntity<PageResponse<ParentDTO>> getAllParent(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page, @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return parentService.getAll(page, size);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ParentDTO>> searchParent(@RequestParam String name) {
        return parentService.search(name);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteParent(@PathVariable UUID id) {
        return parentService.delete(id);
    }
}
