package com.example.TanKhoaLearningCenterBE.web.rest;

import com.example.TanKhoaLearningCenterBE.service.VNPAYService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class VNPAYController {
    private final VNPAYService vnpayService;

    @PostMapping("/createPaymentUrl")
    public String submitOrder(@RequestParam int orderTotal, @RequestParam String orderInfo, HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnpayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }
}
