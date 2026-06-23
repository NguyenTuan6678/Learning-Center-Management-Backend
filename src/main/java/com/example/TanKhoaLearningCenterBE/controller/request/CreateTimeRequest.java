package com.example.TanKhoaLearningCenterBE.controller.request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class CreateTimeRequest {
    private LocalTime timeStart;
    private LocalTime timeEnd;
}
