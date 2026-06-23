package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.controller.request.AdminRegisterRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.AuthenticationResponse;

public interface AdminRegistrationService {
    AuthenticationResponse registerAdmin(AdminRegisterRequest request);
}
