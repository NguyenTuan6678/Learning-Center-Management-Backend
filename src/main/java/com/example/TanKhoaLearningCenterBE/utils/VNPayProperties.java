package com.example.TanKhoaLearningCenterBE.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "vnpay")
public class VNPayProperties {
    private String returnUrl;
    private String payUrl;
    private String apiUrl;
    private String tmnCode;
    private String secretKey;
    private String version;
    private String command;
    private String ordertype;
}
