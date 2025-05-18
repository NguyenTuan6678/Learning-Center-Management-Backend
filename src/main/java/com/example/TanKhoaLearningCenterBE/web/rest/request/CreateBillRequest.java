package com.example.TanKhoaLearningCenterBE.web.rest.request;

import com.example.TanKhoaLearningCenterBE.utils.bill.BillStatus;
import lombok.Data;

@Data
public class CreateBillRequest {
    private String content;
    private BillStatus status;
}
