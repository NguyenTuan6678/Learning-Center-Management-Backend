package com.example.TanKhoaLearningCenterBE.controller;

import com.example.TanKhoaLearningCenterBE.service.AdminRegistrationService;
import com.example.TanKhoaLearningCenterBE.controller.request.AdminRegisterRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
public class AdminRegistrationController {
    private final AdminRegistrationService adminRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(adminRegistrationService.registerAdmin(request));
    }
}
