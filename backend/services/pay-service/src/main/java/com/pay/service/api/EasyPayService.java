package com.pay.service.api;

import com.pay.dto.AliPay;
import jakarta.servlet.http.HttpServletRequest;


public interface EasyPayService {
    AliPay pay(String username); // 生成支付二维码
    String notify(HttpServletRequest request); // 支付成功回调接口
    int query(String username, String id); // 查询支付结果
}