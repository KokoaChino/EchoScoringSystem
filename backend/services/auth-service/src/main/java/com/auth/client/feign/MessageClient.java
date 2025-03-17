package com.auth.client.feign;

import com.auth.client.fallback.MessageClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "message-service",
             path = "/api/message",
             fallback = MessageClientFallback.class)
public interface MessageClient {

    @PostMapping("/send-code-email")
    String sendCodeEmail(@RequestParam("email") String email,
                         @RequestParam("code") String code); // 发送验证码邮件
}