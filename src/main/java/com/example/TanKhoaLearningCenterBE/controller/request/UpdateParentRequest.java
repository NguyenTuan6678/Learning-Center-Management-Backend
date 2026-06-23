package com.example.TanKhoaLearningCenterBE.controller.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateParentRequest {
    private String name;
    private String phoneNumber;
    private String email;
    private UUID accountId;
}
