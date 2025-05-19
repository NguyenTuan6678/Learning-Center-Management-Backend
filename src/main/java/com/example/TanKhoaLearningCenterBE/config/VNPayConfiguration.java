package com.example.TanKhoaLearningCenterBE.config;

import com.example.TanKhoaLearningCenterBE.utils.VNPayProperties;
import com.example.TanKhoaLearningCenterBE.utils.VNPayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;


@Configuration
@RequiredArgsConstructor
public class VNPayConfiguration {
    private final VNPayProperties vnpayProperties;

    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", vnpayProperties.getVersion());
        vnpParamsMap.put("vnp_Command", vnpayProperties.getCommand());
        vnpParamsMap.put("vnp_TmnCode", vnpayProperties.getTmnCode());
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_TxnRef", VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" + VNPayUtil.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderType", vnpayProperties.getOrdertype());
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", vnpayProperties.getReturnUrl());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);

        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);

        return vnpParamsMap;
    }

    public String getSecretKey() {
        return vnpayProperties.getSecretKey();
    }

    public String getPayUrl() {
        return vnpayProperties.getPayUrl();
    }
}