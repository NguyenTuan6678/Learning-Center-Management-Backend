package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.security.JwtService;

import com.example.TanKhoaLearningCenterBE.entity.AccountEntity;
import com.example.TanKhoaLearningCenterBE.repository.AccountRepository;
import com.example.TanKhoaLearningCenterBE.utils.user.Role;
import com.example.TanKhoaLearningCenterBE.controller.request.AdminRegisterRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminRegistrationServiceImpl implements AdminRegistrationService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse registerAdmin(AdminRegisterRequest request) {
        // Make sure there is only 1 admin user exists
        if (accountRepository.existsByRole(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin user already exists in the system.");
        }

        // Create the admin account
        var admin = AccountEntity.builder()
                .userName(request.getUsername())
                .passWord(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();

        var savedAdmin = accountRepository.save(admin);
        var jwtToken = jwtService.generateToken(savedAdmin);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .role(Role.ADMIN.name())
                .userId(savedAdmin.getAccountId())
                .build();
    }
}