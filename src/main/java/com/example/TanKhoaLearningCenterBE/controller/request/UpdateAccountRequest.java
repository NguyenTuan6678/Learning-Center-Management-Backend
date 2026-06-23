package com.example.TanKhoaLearningCenterBE.controller.request;

import com.example.TanKhoaLearningCenterBE.utils.user.Role;
import lombok.Data;

@Data
public class UpdateAccountRequest {
    private String username;
    private String password;
    private Role role;
}
