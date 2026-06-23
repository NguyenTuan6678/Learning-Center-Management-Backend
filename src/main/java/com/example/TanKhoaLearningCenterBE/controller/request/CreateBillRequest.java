package com.example.TanKhoaLearningCenterBE.controller.request;

import com.example.TanKhoaLearningCenterBE.utils.bill.BillStatus;
import lombok.Data;

@Data
public class CreateBillRequest {
    private String content;
    private BillStatus status;
}
