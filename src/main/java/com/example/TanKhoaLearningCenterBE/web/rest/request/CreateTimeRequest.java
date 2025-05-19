package com.example.TanKhoaLearningCenterBE.web.rest.request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class CreateTimeRequest {
    private LocalTime timeStart;
    private LocalTime timeEnd;
}
