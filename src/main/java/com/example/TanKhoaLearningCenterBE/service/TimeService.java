package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.TimeDTO;
import com.example.TanKhoaLearningCenterBE.entity.TimeEntity;
import com.example.TanKhoaLearningCenterBE.repository.TimeRepository;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateTimeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimeService {
    private final TimeRepository timeRepository;

    public ResponseEntity<TimeDTO> createTime(CreateTimeRequest request) {
        TimeEntity timeEntity = new TimeEntity();
        timeEntity.setTimeStart(request.getTimeStart());
        timeEntity.setTimeEnd(request.getTimeEnd());

        timeRepository.save(timeEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TimeDTO(timeEntity));
    }
}
