package com.example.TanKhoaLearningCenterBE.web.rest;

import com.example.TanKhoaLearningCenterBE.dto.DayDTO;
import com.example.TanKhoaLearningCenterBE.service.DayService;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateDayRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/day")
public class DayController {
    private final DayService dayService;

    @PostMapping("/create")
    public ResponseEntity<DayDTO> createDay(@RequestParam CreateDayRequest request) {
        return dayService.createDay(request);
    }
}
