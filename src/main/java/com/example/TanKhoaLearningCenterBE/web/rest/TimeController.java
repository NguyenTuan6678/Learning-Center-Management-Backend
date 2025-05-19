package com.example.TanKhoaLearningCenterBE.web.rest;

import com.example.TanKhoaLearningCenterBE.dto.TimeDTO;
import com.example.TanKhoaLearningCenterBE.service.TimeService;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateTimeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/time")
public class TimeController {
    private final TimeService timeService;

    @PostMapping("/create")
    public ResponseEntity<TimeDTO> createTime(@RequestParam CreateTimeRequest request) {
        return timeService.createTime(request);
    }
}
