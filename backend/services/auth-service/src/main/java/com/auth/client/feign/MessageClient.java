package com.auth.client.feign;

import com.auth.client.fallback.MessageClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;


@FeignClient(name = "message-service",
             path = "/api/message",
             fallback = MessageClientFallback.class)
public interface MessageClient {

    @PostMapping("/send-code-email")
    String sendCodeEmail(@RequestParam("email") String email,
                         @RequestParam("code") String code); // 发送验证码邮件

    @PostMapping("/send-mq-message")
    void sendMqMessage(@RequestParam("exchange") String exchange,
                       @RequestParam("key") String key,
                       @RequestBody Map<String, String> msg); // 发送支付成功通知
}