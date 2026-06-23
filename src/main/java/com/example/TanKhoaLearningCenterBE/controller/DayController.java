package com.example.TanKhoaLearningCenterBE.controller;

import com.example.TanKhoaLearningCenterBE.dto.DayDTO;
import com.example.TanKhoaLearningCenterBE.service.DayService;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateDayRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/day")
public class DayController {
    private final DayService dayService;

    @PostMapping("/create")
    public ResponseEntity<DayDTO> createDay(@RequestBody CreateDayRequest request) {
        return dayService.createDay(request);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<DayDTO>> getAllDays() {
        return dayService.getAllDays();
    }
}
