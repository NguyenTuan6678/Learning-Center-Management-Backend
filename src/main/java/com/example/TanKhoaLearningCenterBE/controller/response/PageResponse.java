package com.example.TanKhoaLearningCenterBE.controller.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PageResponse<T> {
    private long count;
    private List<T> rows;
    private int page;
    private int size;
}
