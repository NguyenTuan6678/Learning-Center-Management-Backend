package com.example.TanKhoaLearningCenterBE.controller;

import com.example.TanKhoaLearningCenterBE.dto.TimeDTO;
import com.example.TanKhoaLearningCenterBE.service.TimeService;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateTimeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/time")
public class TimeController {
    private final TimeService timeService;

    @PostMapping("/create")
    public ResponseEntity<TimeDTO> createTime(@RequestBody CreateTimeRequest request) {
        return timeService.createTime(request);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<TimeDTO>> getAllTimes() {
        return timeService.getAllTimes();
    }
}
