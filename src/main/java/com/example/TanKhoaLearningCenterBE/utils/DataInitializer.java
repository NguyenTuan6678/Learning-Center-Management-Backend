package com.example.TanKhoaLearningCenterBE.utils;

import com.example.TanKhoaLearningCenterBE.entity.PaymentMethodEntity;
import com.example.TanKhoaLearningCenterBE.repository.PaymentMethodRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final PaymentMethodRepository paymentMethodRepository;

    @PostConstruct
    public void initPaymentMethods() {
        createPaymentMethodIfNotFound("VNPAY", "Thanh toán qua VNPay");
        createPaymentMethodIfNotFound("CASH", "Tiền mặt");
    }

    private void createPaymentMethodIfNotFound(String code, String description) {
        if (!paymentMethodRepository.existsByPayMethod(code)) {
            PaymentMethodEntity method = new PaymentMethodEntity();
            method.setPayMethod(code);
            paymentMethodRepository.save(method);
        }
    }
}
