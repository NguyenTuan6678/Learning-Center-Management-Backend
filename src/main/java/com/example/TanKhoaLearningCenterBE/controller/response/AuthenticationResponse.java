package com.example.TanKhoaLearningCenterBE.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("role")
    private String role;

    @JsonProperty("id")
    private UUID userId;

    @JsonProperty("studentId")
    private UUID studentId;

    @JsonProperty("teacherId")
    private UUID teacherId;
}
