package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.entity.AccountEntity;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import com.example.TanKhoaLearningCenterBE.repository.AccountRepository;
import com.example.TanKhoaLearningCenterBE.repository.StudentRepository;
import com.example.TanKhoaLearningCenterBE.repository.TeacherRepository;
import com.example.TanKhoaLearningCenterBE.utils.user.Role;
import com.example.TanKhoaLearningCenterBE.web.rest.request.AuthenticationRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.RegisterRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = AccountEntity.builder()
                .userName(request.getUsername())
                .passWord(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        var savedUser = accountRepository.save(user);
        return AuthenticationResponse.builder()
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = accountRepository.findByUserNameContainingIgnoreCase(request.getUsername())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        UUID studentId = null;
        if (user.getRole() == Role.STUDENT) {
            studentId = studentRepository.findByAccountIds_AccountId(user.getAccountId())
                    .map(StudentEntity::getStudentId)
                    .orElse(null);
        }


        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .role(user.getRole().name())
                .userId(user.getAccountId())
                .studentId(studentId)
                .build();
    }
}
