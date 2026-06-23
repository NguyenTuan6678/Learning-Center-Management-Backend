package com.example.TanKhoaLearningCenterBE.controller.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class FileUploadResponse {
    private String message;
    private int successfulCount;
    private List<String> errors;
}
