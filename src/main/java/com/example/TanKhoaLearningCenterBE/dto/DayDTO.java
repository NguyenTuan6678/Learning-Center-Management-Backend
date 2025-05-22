package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.DayEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class DayDTO {
    private UUID dayId;
    private String day;

    public DayDTO(DayEntity day) {
        this.dayId = day.getDayId();
        this.day = day.getDay();
    }
}
