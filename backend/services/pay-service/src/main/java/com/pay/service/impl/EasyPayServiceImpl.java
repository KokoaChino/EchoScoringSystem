package com.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.common.client.feign.AuthClient;
import com.common.entity.AuthenticationDTO;
import com.common.entity.User;
import com.common.util.KeyGeneratorUtil;
import com.pay.client.MessageClient;
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
    private Config config;

    @Resource
    private StringRedisTemplate template;

    @Resource
    private PayMapper payMapper;

    @Resource
    private MessageClient messageClient;

    @Resource
    private AuthClient authClient;

    private static final String LOCK_PREFIX = "lock-pay：";
    private static final String CACHE_PREFIX = "pay:order：";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AliPay pay(String username) { // 生成支付二维码
        String lockKey = LOCK_PREFIX + username;
        String cacheKey = CACHE_PREFIX + username;
        try {
            Boolean isLocked = template.opsForValue().setIfAbsent(lockKey, UUID.randomUUID().toString(), 3, TimeUnit.SECONDS);
            if (Boolean.TRUE.equals(isLocked)) {
                try {
                    if (Boolean.TRUE.equals(template.hasKey(cacheKey))) {
                        String json = template.opsForValue().get(cacheKey);
                        return JSON.parseObject(json, new TypeReference<AliPay>() {});
                    }
                    Order existingOrder = payMapper.findLatestOrderByUsername(username);
                    if (existingOrder != null && !isOrderExpired(existingOrder)) {
                        return new AliPay(
                                existingOrder.getId(),
                                username,
                                PayUtil.urlToQrcode(existingOrder.getQrUrl())
                        );
                    }
                    String orderId = username + "-" + KeyGeneratorUtil.generate(16);
                    Factory.setOptions(config);
                    AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace().preCreate("声骸评分系统VIP", orderId, "0.01");
                    JSONObject jsonObject = JSON.parseObject(response.getHttpBody());
                    String qrUrl = jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
                    AliPay aliPay = new AliPay(orderId, username, PayUtil.urlToQrcode(qrUrl));
                    // 删除旧订单并插入新订单
                    payMapper.deleteOrderByUsername(username);
                    payMapper.insertOrder(new Order(orderId, username, LocalDateTime.now(), qrUrl));
                    int expireMinutes = 30 + new Random().nextInt(3);
                    template.opsForValue().set(cacheKey, JSON.toJSONString(aliPay), expireMinutes, TimeUnit.MINUTES);
                    // 发送延迟消息（30分钟后检查支付状态）
                    Map<String, String> msg = new HashMap<>(){{
                        put("id", UUID.randomUUID() + "-FAILED");
                        put("outTradeNo", orderId);
                        put("username", username);
                        put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    }};
                    messageClient.sendDelayedMqMessage("e1", "pay_failed", msg, "1800000");
                    return aliPay;
                } finally {
                    template.delete(lockKey);
                }
            } else {
                Thread.sleep(1000);
                return pay(username);
            }
        } catch (Exception e) {
            log.error("支付二维码生成失败，用户：{}", username, e);
            return null;
        }
    }

    @Override
    public String notify(HttpServletRequest request) { // 支付成功回调接口
        String outTradeNo = request.getParameter("out_trade_no");
        String username = outTradeNo.substring(0, outTradeNo.indexOf('-'));
        String cacheKey = CACHE_PREFIX + username;
        if (Boolean.TRUE.equals(template.hasKey(cacheKey))) {
            template.delete(cacheKey);
        }
        updateVipUser(username, outTradeNo);
        return username + "：Success";
    }

    @Override
    public int query(String username, String id) { // 查询支付结果
        User user = authClient.getUser(AuthenticationDTO.getBaseDTO(username)).getData();
        if (user.getVip()) return 1;
        try {
            Factory.setOptions(config);
            AlipayTradeQueryResponse query = Factory.Payment.Common().query(id);
            JSONObject jsonObject = JSONObject.parseObject(query.getHttpBody());
            System.out.println(id + "\n" + jsonObject);
            if (jsonObject.getJSONObject("alipay_trade_query_response").get("code").equals("10000")) {
                updateVipUser(username, id);
                template.delete(CACHE_PREFIX + username);
                return 1;
            }
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    private boolean isOrderExpired(Order order) {
        return Duration.between(order.getCreateTime(), LocalDateTime.now()).toMinutes() > 30;
    }

    private void updateVipUser(String username, String outTradeNo) {
        Map<String, String> msg = new HashMap<>(){{
            put("id", UUID.randomUUID() + "-SUCCESS");
            put("outTradeNo", outTradeNo);
            put("username", username);
        }};
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setUsername(username);
        authenticationDTO.setExtra(msg);
        authClient.updateVipUser(authenticationDTO);
    }
}