package com.example.TanKhoaLearningCenterBE.controller.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
