package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.BillDTO;
import com.example.TanKhoaLearningCenterBE.entity.BillDetailEntity;
import com.example.TanKhoaLearningCenterBE.entity.BillEntity;
import com.example.TanKhoaLearningCenterBE.repository.BillDetailRepository;
import com.example.TanKhoaLearningCenterBE.repository.BillRepository;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateBillRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final BillDetailRepository billDetailRepository;

    @Override
    public ResponseEntity<BillDTO> create(CreateBillRequest request) {
        var billd = new BillEntity();
        billd.setBillContent(request.getContent());
        billd.setBillStatus(request.getStatus());
        var saveBilld = billRepository.save(billd);

        var billDetail =  new BillDetailEntity();
        billDetail.setBill(billd);
        billDetail.setDescription("");
        billDetail.setAmount(0.0);
        billDetail.setCurrency("VND");
        billDetail.setPaymentStatus(request.getStatus().toString());

        billDetailRepository.save(billDetail);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BillDTO(saveBilld));
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
