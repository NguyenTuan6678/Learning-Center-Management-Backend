package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.BillDTO;
import com.example.TanKhoaLearningCenterBE.entity.BillDetailEntity;
import com.example.TanKhoaLearningCenterBE.entity.BillEntity;
import com.example.TanKhoaLearningCenterBE.entity.PaymentEntity;
import com.example.TanKhoaLearningCenterBE.entity.PaymentMethodEntity;
import com.example.TanKhoaLearningCenterBE.repository.BillDetailRepository;
import com.example.TanKhoaLearningCenterBE.repository.BillRepository;
import com.example.TanKhoaLearningCenterBE.repository.PaymentMethodRepository;
import com.example.TanKhoaLearningCenterBE.repository.PaymentRepository;
import com.example.TanKhoaLearningCenterBE.controller.request.CreateBillRequest;
import com.example.TanKhoaLearningCenterBE.controller.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailRepository billDetailRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public ResponseEntity<BillDTO> create(CreateBillRequest request) {
        try {
            // Validate request
            if (request.getContent() == null || request.getStatus() == null) {
                throw new IllegalArgumentException("Content and status are required");
            }

            // Tạo bill
            BillEntity bill = new BillEntity();
            bill.setBillContent(request.getContent());
            bill.setBillStatus(request.getStatus());

            // Tạo bill detail
            BillDetailEntity billDetail = new BillDetailEntity();
            billDetail.setBill(bill);
            billDetail.setDescription("");
            billDetail.setAmount(0.0);
            billDetail.setCurrency("VND");
            billDetail.setPaymentStatus(request.getStatus().toString());

            // Tìm payment method - thêm fallback nếu VNPAY không tồn tại
            PaymentMethodEntity paymentMethod = paymentMethodRepository.findByPayMethod("VNPAY")
                    .orElseGet(() -> paymentMethodRepository.findByPayMethod("CASH")
                            .orElseThrow(() -> new RuntimeException("No payment method available")));

            // Tạo payment
            PaymentEntity payment = new PaymentEntity();
            payment.setBill(bill);
            payment.setStatus(request.getStatus());
            payment.setPaymentMethod(paymentMethod);
            payment.setAmount(0.0);

            // Lưu vào DB
            billRepository.save(bill);
            billDetailRepository.save(billDetail);
            paymentRepository.save(payment);

            return ResponseEntity.status(HttpStatus.CREATED).body(new BillDTO(bill));

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create bill: " + e.getMessage()
            );
        }
    }

    @Override
    public ResponseEntity<PageResponse<BillDTO>> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BillEntity> bills = billRepository.findAll(pageable);
        List<BillDTO> rows = bills.getContent().stream().map(BillDTO::new).toList();
        var response = new PageResponse<BillDTO>();
        response.setCount(bills.getTotalElements());
        response.setRows(rows);
        response.setPage(page);
        response.setSize(size);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
