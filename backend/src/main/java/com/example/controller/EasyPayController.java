package com.example.controller;

import com.example.dto.AliPay;
import com.example.service.api.EasyPayService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pay")
@Slf4j
@AllArgsConstructor
public class EasyPayController {

    @Resource
    EasyPayService service;

    @PostMapping("/pay")
    public AliPay pay(@RequestParam("username") String username) { // 生成支付二维码
        return service.pay(username);
    }

    @PostMapping("/notify")
    public String notify(HttpServletRequest request) { // 支付成功回调接口
        return service.notify(request);
    }

    @PostMapping("/query")
    public int query(@RequestParam("username") String username,
                     @RequestParam("id") String id) { // 查询支付结果
        return service.query(username, id);
    }
}