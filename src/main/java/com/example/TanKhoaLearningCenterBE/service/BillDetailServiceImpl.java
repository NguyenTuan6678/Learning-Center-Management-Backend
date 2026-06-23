package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.BillDetailDTO;
import com.example.TanKhoaLearningCenterBE.entity.BillDetailEntity;
import com.example.TanKhoaLearningCenterBE.entity.BillEntity;
import com.example.TanKhoaLearningCenterBE.entity.ParentEntity;
import com.example.TanKhoaLearningCenterBE.entity.StudentEntity;
import com.example.TanKhoaLearningCenterBE.exception.ParentNotFoundException;
import com.example.TanKhoaLearningCenterBE.exception.StudentNotFoundException;
import com.example.TanKhoaLearningCenterBE.repository.BillDetailRepository;
import com.example.TanKhoaLearningCenterBE.repository.BillRepository;
import com.example.TanKhoaLearningCenterBE.repository.ParentRepository;
import com.example.TanKhoaLearningCenterBE.repository.StudentRepository;
import com.example.TanKhoaLearningCenterBE.utils.bill.BillStatus;
import com.example.TanKhoaLearningCenterBE.controller.request.UpdateBillDetailsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillDetailServiceImpl implements BillDetailService {
    private final BillRepository billRepository;
    private final BillDetailRepository billDetailRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;

    @Override
    public ResponseEntity<BillDetailDTO> update(UUID id, UpdateBillDetailsRequest request) {
        BillDetailEntity billDetail = billDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillDetail not found for Bill ID: " + id));

        BillEntity bill = billDetail.getBill();billDetail.setDescription(request.getDescription());
        billDetail.setAmount(request.getAmount());
        billDetail.setCurrency(request.getCurrency());
        if (request.getPaymentStatus() != null) {
            billDetail.setPaymentStatus(request.getPaymentStatus());

            BillStatus newBillStatus = mapPaymentStatusToBillStatus(request.getPaymentStatus());
            if (newBillStatus != null && bill.getBillStatus() != newBillStatus) {
                bill.setBillStatus(newBillStatus);
                billRepository.save(bill); // Lưu lại BillEntity với trạng thái mới
            }
        }

        StudentEntity student = studentRepository.findById(request.getStudentId()).orElseThrow(StudentNotFoundException::new);
//        ParentEntity parent = parentRepository.findById(request.getParentId()).orElseThrow(ParentNotFoundException::new);

        billDetail.setStudent(student);
//        billDetail.setParent(parent);

        if (request.getParentId() != null) {
            ParentEntity parent = parentRepository.findById(request.getParentId())
                    .orElseThrow(ParentNotFoundException::new);
            billDetail.setParent(parent);
        } else {
            billDetail.setParent(null); // ✅ Cho phép bỏ parent nếu không có
        }

        BillDetailEntity saved = billDetailRepository.save(billDetail);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BillDetailDTO(saved));
    }

    @Override
    public ResponseEntity<BillDetailDTO> get(UUID id) {
        Optional<BillDetailEntity> optionalBillDetail = billDetailRepository.findById(id);
        if (optionalBillDetail.isPresent()) {
            return ResponseEntity.ok(new BillDetailDTO(optionalBillDetail.get()));
        }
        throw new RuntimeException("Bill not found");
    }

    @Override
    public ResponseEntity<BillDetailDTO> getBillDetailForStudent(UUID billId, UUID studentId) {
        BillDetailEntity billDetail = billDetailRepository.findById(billId).orElseThrow(() -> new RuntimeException("Billdetail not found for ID: " + billId));
        if (!billDetail.getStudent().getStudentId().equals(studentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(new BillDetailDTO(billDetail));
    }

    @Override
    public ResponseEntity<List<BillDetailDTO>> getAllBillDetailsByStudent(UUID studentId) {
        List<BillDetailEntity> billDetails = billDetailRepository.findAllByStudent_StudentId(studentId);
        return ResponseEntity.ok(
                billDetails.stream()
                        .map(BillDetailDTO::new)
                        .collect(Collectors.toList())
        );
    }

    private BillStatus mapPaymentStatusToBillStatus(String paymentStatus) {
        return switch (paymentStatus.toUpperCase()) {
            case "PENDING" -> BillStatus.PENDING;
            case "PAID" -> BillStatus.PAID;
            case "FAILED" -> BillStatus.FAILED;
            case "CANCELLED" -> BillStatus.CANCELLED;
            default -> null;
        };
    }
}
