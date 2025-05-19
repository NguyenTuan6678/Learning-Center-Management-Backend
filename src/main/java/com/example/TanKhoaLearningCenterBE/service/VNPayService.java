package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.config.VNPayConfiguration;
import com.example.TanKhoaLearningCenterBE.dto.BillDTO;
import com.example.TanKhoaLearningCenterBE.dto.BillDetailDTO;
import com.example.TanKhoaLearningCenterBE.dto.PaymentDTO;
//import com.example.TanKhoaLearningCenterBE.entity.BillDetailEntity;
import com.example.TanKhoaLearningCenterBE.entity.BillEntity;
import com.example.TanKhoaLearningCenterBE.entity.PaymentEntity;
import com.example.TanKhoaLearningCenterBE.entity.PaymentMethodEntity;
import com.example.TanKhoaLearningCenterBE.repository.BillRepository;
import com.example.TanKhoaLearningCenterBE.repository.PaymentMethodRepository;
import com.example.TanKhoaLearningCenterBE.repository.PaymentRepository;
import com.example.TanKhoaLearningCenterBE.utils.VNPayUtil;
import com.example.TanKhoaLearningCenterBE.utils.bill.BillStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VNPayService {
    private final VNPayConfiguration vnPayConfig;
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentDTO.VNPayResponse createVNPayPayment(UUID billId, HttpServletRequest request) {
        // 1. Validate bill và payment
        BillEntity bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (bill.getPayment() == null) {
            throw new RuntimeException("Payment not initialized for this bill");
        }

        // 2. Kiểm tra số tiền từ BillDetail
        if (bill.getBillDetailEntity() == null || bill.getBillDetailEntity().getAmount() <= 0) {
            throw new RuntimeException("Invalid bill amount");
        }

        double amount = bill.getBillDetailEntity().getAmount();

        // 3. Cập nhật payment method
        PaymentMethodEntity vnpayMethod = paymentMethodRepository.findByPayMethod("VNPAY")
                .orElseThrow(() -> new RuntimeException("VNPAY method not configured"));

        PaymentEntity payment = bill.getPayment();
        payment.setPaymentMethod(vnpayMethod);
        payment.setAmount(amount);
        payment.setStatus(BillStatus.PENDING);
        paymentRepository.save(payment);

        // 4. Tạo URL thanh toán VNPay
        Map<String, String> vnpParams = vnPayConfig.getVNPayConfig();
        vnpParams.put("vnp_Amount", String.valueOf((long)(amount * 100)));
        vnpParams.put("vnp_OrderInfo", "Thanh toán hóa đơn: " + billId);
        vnpParams.put("vnp_TxnRef", payment.getPaymentId().toString());
        vnpParams.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        String paymentUrl = buildVNPayPaymentUrl(vnpParams);

        return PaymentDTO.VNPayResponse.builder()
                .code("00")
                .message("Success")
                .paymentUrl(paymentUrl)
                .build();
    }

    private String buildVNPayPaymentUrl(Map<String, String> params) {
        String queryUrl = VNPayUtil.getPaymentURL(params, true);
        String hashData = VNPayUtil.getPaymentURL(params, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        return vnPayConfig.getPayUrl() + "?" + queryUrl + "&vnp_SecureHash=" + vnpSecureHash;
    }

    public PaymentDTO.PaymentResponse getPaymentStatus(UUID paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> {
                    log.error("Payment not found with ID: {}", paymentId);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
                });

        BillEntity bill = payment.getBill();

        return PaymentDTO.PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .billId(bill.getBillId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod().getPayMethod())
                .status(payment.getStatus().toString())
                .createdAt(payment.getUpdatedAt())
                .bill(new BillDTO(bill))
                .billDetail(bill.getBillDetailEntity() != null ?
                        new BillDetailDTO(bill.getBillDetailEntity()) : null)
                .build();
    }

    @Transactional
    public PaymentDTO.PaymentResponse handleVNPayCallback(HttpServletRequest request) {
        // 1. Chuyển đổi request parameters sang Map
        Map<String, String> params = Collections.list(request.getParameterNames())
                .stream()
                .collect(Collectors.toMap(
                        paramName -> paramName,
                        request::getParameter
                ));

        log.info("Received VNPay callback with params: {}", params);

        // 2. Xác thực callback
        if (!validateVNPayCallback(params)) {
            log.error("VNPay callback validation failed");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid VNPay callback signature");
        }

        // 3. Xử lý trạng thái thanh toán
        String vnpResponseCode = params.get("vnp_ResponseCode");
        UUID paymentId = UUID.fromString(params.get("vnp_TxnRef"));

        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> {
                    log.error("Payment not found with ID: {}", paymentId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment record not found");
                });

        BillEntity bill = payment.getBill();
        boolean isSuccess = "00".equals(vnpResponseCode);

        // 4. Cập nhật trạng thái
        updatePaymentStatus(payment, bill, isSuccess);

        // 5. Log kết quả
        log.info("Processed VNPay callback for payment {}: {}", paymentId,
                isSuccess ? "SUCCESS" : "FAILED");

        // 6. Trả về response
        return buildPaymentResponse(payment, bill);
    }

    private boolean validateVNPayCallback(Map<String, String> params) {
        try {
            String vnpSecureHash = params.get("vnp_SecureHash");
            if (vnpSecureHash == null || vnpSecureHash.isEmpty()) {
                return false;
            }

            // Tạo chuỗi dữ liệu để hash
            String hashData = params.entrySet()
                    .stream()
                    .filter(entry -> !entry.getKey().equals("vnp_SecureHash"))
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> String.format("%s=%s",
                            URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII),
                            URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII)))
                    .collect(Collectors.joining("&"));

            // Tạo chữ ký
            String calculatedHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
            return calculatedHash.equalsIgnoreCase(vnpSecureHash);
        } catch (Exception e) {
            log.error("Error validating VNPay callback", e);
            return false;
        }
    }

    private void updatePaymentStatus(PaymentEntity payment, BillEntity bill, boolean isSuccess) {
        if (isSuccess) {
            payment.setStatus(BillStatus.PAID);
            bill.setBillStatus(BillStatus.PAID);

            if (bill.getBillDetailEntity() != null) {
                bill.getBillDetailEntity().setPaymentStatus("PAID");
            }
        } else {
            payment.setStatus(BillStatus.FAILED);
            bill.setBillStatus(BillStatus.FAILED);

            if (bill.getBillDetailEntity() != null) {
                bill.getBillDetailEntity().setPaymentStatus("FAILED");
            }
        }

        paymentRepository.save(payment);
        billRepository.save(bill);
    }

    private PaymentDTO.PaymentResponse buildPaymentResponse(PaymentEntity payment, BillEntity bill) {
        return PaymentDTO.PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod().getPayMethod())
                .bill(new BillDTO(bill))
                .billDetail(bill.getBillDetailEntity() != null ?
                        new BillDetailDTO(bill.getBillDetailEntity()) : null)
                .status(payment.getStatus().toString())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
