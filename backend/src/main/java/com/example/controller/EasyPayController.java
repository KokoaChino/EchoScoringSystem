package com.example.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.example.dto.AliPay;
import com.example.service.api.AuthorizeService;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.util.Base64;


@RestController
@RequestMapping("/pay")
@Slf4j
@AllArgsConstructor
public class EasyPayController {

    private final Config config;

    @Resource
    AuthorizeService service;

    @PostMapping("/pay")
    public AliPay pay(@RequestParam("username") String username) { // 生成支付二维码
        try {
            String id = username + "-" + System.currentTimeMillis();
            Factory.setOptions(config);
            AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace().preCreate("声骸评分系统VIP", id, "39.99");
            String httpBody = response.getHttpBody();
            JSONObject jsonObject = JSONObject.parseObject(httpBody);
            String qrUrl = jsonObject.getJSONObject("alipay_trade_precreate_response").get("qr_code").toString();
            QrConfig config = new QrConfig(500, 500);
            config.setErrorCorrection(ErrorCorrectionLevel.H);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String targetType = "jpg";
            QrCodeUtil.generate(qrUrl, config, targetType, baos);
            String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
            return new AliPay(id, username, "data:" + targetType + ";base64," + base64);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/notify")
    public String notify(HttpServletRequest request) { // 支付成功回调接口
        String outTradeNo = request.getParameter("out_trade_no");
        String username = outTradeNo.substring(0, outTradeNo.indexOf('-'));
        service.updateUserVip(username);
        return username + "：Success";
    }

    @PostMapping("/query")
    public int query(@RequestParam("username") String username,
                     @RequestParam("id") String id) { // 查询支付结果
        try {
            Factory.setOptions(config);
            AlipayTradeQueryResponse query = Factory.Payment.Common().query(id);
            JSONObject jsonObject = JSONObject.parseObject(query.getHttpBody());
            if (jsonObject.getJSONObject("alipay_trade_query_response").get("code").equals("10000")) {
                service.updateUserVip(username);
                return 1;
            }
            else return 0;
        } catch (Exception e) {
            return -1;
        }
    }
}