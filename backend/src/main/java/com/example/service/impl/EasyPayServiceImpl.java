package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.example.dto.AliPay;
import com.example.entity.order.Order;
import com.example.mapper.MqMapper;
import com.example.mapper.PayMapper;
import com.example.service.api.EasyPayService;
import com.example.util.PayUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class EasyPayServiceImpl implements EasyPayService {

    @Resource
    Config config;

    @Resource
    StringRedisTemplate template;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    PayMapper payMapper;

    @Resource
    MqMapper mqMapper;

    @Override
    public AliPay pay(String username) { // 生成支付二维码
        if (Boolean.TRUE.equals(template.hasKey(username))) { // Redis 里有未过期的订单，直接返回
            String json = template.opsForValue().get(username);
            return JSON.parseObject(json, new TypeReference<AliPay>() {});
        } else { // 否则，再去数据库找
            Order order = payMapper.findOrderByUsername(username);
            if (order != null) {
                if (Duration.between(order.getCreateTime(), LocalDateTime.now()).toMinutes() <= 30)
                    return new AliPay(order.getId(), username, PayUtil.urlToQrcode(order.getQrUrl()));
            }
            try {
                String id = username + "-" + System.currentTimeMillis();
                Factory.setOptions(config);
                AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace().preCreate("声骸评分系统VIP", id, "39.99");
                String httpBody = response.getHttpBody();
                JSONObject jsonObject = JSONObject.parseObject(httpBody);
                String qrUrl = jsonObject.getJSONObject("alipay_trade_precreate_response").get("qr_code").toString();
                AliPay aliPay = new AliPay(id, username, PayUtil.urlToQrcode(qrUrl));
                payMapper.deleteOrderByUsername(username);
                payMapper.insertOrder(new Order(id, username, LocalDateTime.now(), qrUrl));
                template.opsForValue().set(username, JSON.toJSONString(aliPay), 30, TimeUnit.MINUTES);
                Map<String, String> msg = new HashMap<>(
                        Map.ofEntries(
                                Map.entry("id", id + "-FAILED"),
                                Map.entry("outTradeNo", id),
                                Map.entry("username", username),
                                Map.entry("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        )
                );
                mqMapper.insertMessageIdLog(msg.get("id"), 0);
                rabbitTemplate.convertAndSend("e1", "pay_failed", msg, message -> {
                    message.getMessageProperties().setExpiration("1800000"); // 延迟时间 30min
                    return message;
                });
                return aliPay;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public String notify(HttpServletRequest request) { // 支付成功回调接口
        String outTradeNo = request.getParameter("out_trade_no");
        String username = outTradeNo.substring(0, outTradeNo.indexOf('-'));
        if (Boolean.TRUE.equals(template.hasKey(username))) {
            template.delete(username);
        }
        payMapper.updateVipUser(username);
        Map<String, String> msg = new HashMap<>(
                Map.ofEntries(
                        Map.entry("id", outTradeNo + "-SUCCESS"),
                        Map.entry("outTradeNo", outTradeNo),
                        Map.entry("username", username)
                )
        );
        mqMapper.insertMessageIdLog(msg.get("id"), 0);
        rabbitTemplate.convertAndSend("e1", "pay_success", msg);
        return username + "：Success";
    }

    @Override
    public int query(String username, String id) { // 查询支付结果
        if (payMapper.isVipUser(username)) return 1;
        try {
            Factory.setOptions(config);
            AlipayTradeQueryResponse query = Factory.Payment.Common().query(id);
            JSONObject jsonObject = JSONObject.parseObject(query.getHttpBody());
            if (jsonObject.getJSONObject("alipay_trade_query_response").get("code").equals("10000")) {
                payMapper.updateVipUser(username);
                return 1;
            }
            else return 0;
        } catch (Exception e) {
            return -1;
        }
    }
}