package com.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.pay.client.feign.AuthClient;
import com.pay.client.feign.MessageClient;
import com.pay.dto.AliPay;
import com.pay.entity.Order;
import com.pay.mapper.PayMapper;
import com.pay.service.api.EasyPayService;
import com.pay.util.PayUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class EasyPayServiceImpl implements EasyPayService {

    @Resource
    Config config;

    @Resource
    StringRedisTemplate template;

    @Resource
    PayMapper payMapper;

    @Resource
    MessageClient messageClient;

    @Resource
    AuthClient authClient;

    private static final String LOCK_PREFIX = "lock:pay:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AliPay pay(String username) { // 生成支付二维码
        String lockKey = LOCK_PREFIX + username;
        try {
            // 获取分布式锁，防止缓存击穿
            Boolean isLocked = template.opsForValue().setIfAbsent(lockKey, UUID.randomUUID().toString(), 3, TimeUnit.SECONDS);
            if (Boolean.TRUE.equals(isLocked)) {
                try {
                    if (Boolean.TRUE.equals(template.hasKey(username))) {
                        String json = template.opsForValue().get(username);
                        return JSON.parseObject(json, new TypeReference<AliPay>() {});
                    }
                    Order existingOrder = payMapper.findOrderByUsername(username);
                    if (existingOrder != null && !isOrderExpired(existingOrder)) {
                        return new AliPay(
                                existingOrder.getId(),
                                username,
                                PayUtil.urlToQrcode(existingOrder.getQrUrl())
                        );
                    }
                    String orderId = username + "-" + System.currentTimeMillis();
                    Factory.setOptions(config);
                    AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace().preCreate("声骸评分系统VIP", orderId, "39.99");
                    String httpBody = response.getHttpBody();
                    JSONObject jsonObject = JSON.parseObject(httpBody);
                    String qrUrl = jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
                    AliPay aliPay = new AliPay(orderId, username, PayUtil.urlToQrcode(qrUrl));
                    // 删除旧订单并插入新订单（事务内操作）
                    payMapper.deleteOrderByUsername(username);
                    payMapper.insertOrder(new Order(orderId, username, LocalDateTime.now(), qrUrl));
                    String aliPayJson = JSON.toJSONString(aliPay); // 设置缓存（带随机过期时间，防雪崩）
                    int expireMinutes = 30 + new Random().nextInt(3);
                    template.opsForValue().set(username, aliPayJson, expireMinutes, TimeUnit.MINUTES);
                    // 发送延迟消息（30分钟后检查支付状态）
                    Map<String, String> msg = new HashMap<>();
                    msg.put("id", UUID.randomUUID() + "-FAILED");
                    msg.put("outTradeNo", orderId);
                    msg.put("username", username);
                    msg.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    messageClient.insertMessageIdLog(msg.get("id"), 0);
                    messageClient.sendDelayedMqMessage("e1", "pay_failed", msg, "1800000"); // 30分钟延迟
                    return aliPay;
                } finally {
                    template.delete(lockKey); // 释放锁
                }
            } else {
                // 获取锁失败，等待后重试（适用于低并发场景）
                Thread.sleep(1000);
                return pay(username);
            }
        } catch (Exception e) {
            log.error("支付二维码生成失败，用户：{}", username, e);
            return null;
        }
    }

    private boolean isOrderExpired(Order order) {
        return Duration.between(order.getCreateTime(), LocalDateTime.now()).toMinutes() > 30;
    }

    @Override
    public String notify(HttpServletRequest request) { // 支付成功回调接口
        String outTradeNo = request.getParameter("out_trade_no");
        String username = outTradeNo.substring(0, outTradeNo.indexOf('-'));
        if (Boolean.TRUE.equals(template.hasKey(username))) {
            template.delete(username);
        }
        authClient.updateVipUser(username);
        Map<String, String> msg = new HashMap<>(
                Map.ofEntries(
                        Map.entry("id", UUID.randomUUID() + "-SUCCESS"),
                        Map.entry("outTradeNo", outTradeNo),
                        Map.entry("username", username)
                )
        );
        messageClient.insertMessageIdLog(msg.get("id"), 0);
        messageClient.sendMqMessage("e1", "pay_success", msg);
        return username + "：Success";
    }

    @Override
    public int query(String username, String id) { // 查询支付结果
        if (authClient.isVip(username)) return 1;
        try {
            Factory.setOptions(config);
            AlipayTradeQueryResponse query = Factory.Payment.Common().query(id);
            JSONObject jsonObject = JSONObject.parseObject(query.getHttpBody());
            if (jsonObject.getJSONObject("alipay_trade_query_response").get("code").equals("10000")) {
                authClient.updateVipUser(username);
                return 1;
            }
            else return 0;
        } catch (Exception e) {
            return -1;
        }
    }
}