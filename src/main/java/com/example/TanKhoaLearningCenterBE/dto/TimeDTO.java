package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.TimeEntity;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class TimeDTO {
    private UUID id;
    private LocalTime timeStart;
    private LocalTime timeEnd;

    public TimeDTO(TimeEntity time) {
        this.id = time.getTimeId();
        this.timeStart = time.getTimeStart();
        this.timeEnd = time.getTimeEnd();
    }
}
