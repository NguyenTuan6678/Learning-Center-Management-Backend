package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.DayDTO;
import com.example.TanKhoaLearningCenterBE.entity.DayEntity;
import com.example.TanKhoaLearningCenterBE.repository.DayRepository;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateDayRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DayService {

    private final DayRepository dayRepository;

    public ResponseEntity<DayDTO> createDay(CreateDayRequest request) {
        DayEntity day = new DayEntity();
        day.setDay(request.getDay());
        dayRepository.save(day);

        return ResponseEntity.status(HttpStatus.CREATED).body(new DayDTO(day));
    }

    public ResponseEntity<List<DayDTO>> getAllDays() {
        List<DayEntity> days = dayRepository.findAll();
        List<DayDTO> dayDTOs = days.stream().map(DayDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dayDTOs);
    }
}
