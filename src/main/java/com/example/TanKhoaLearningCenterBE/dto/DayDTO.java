package com.example.TanKhoaLearningCenterBE.dto;

import com.example.TanKhoaLearningCenterBE.entity.DayEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class DayDTO {
    private UUID id;
    private String name;

    public DayDTO(DayEntity day) {
        this.id = day.getDayId();
        this.name = day.getDay();
    }
}
