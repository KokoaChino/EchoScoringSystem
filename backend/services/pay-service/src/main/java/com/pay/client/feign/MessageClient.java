package com.pay.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;


@FeignClient(name = "message-service",
             path = "/api/message")
public interface MessageClient {

    @PostMapping("/insert-message-id-log")
    void insertMessageIdLog(@RequestParam("messageId") String messageId,
                            @RequestParam("status") int status); // 插入消息ID日志

    @PostMapping("/send-mq-message")
    void sendMqMessage(@RequestParam("exchange") String exchange,
                       @RequestParam("key") String key,
                       @RequestBody Map<String, String> msg); // 发送支付成功通知

    @PostMapping("/send-delayed-mq-message")
    void sendDelayedMqMessage(@RequestParam("exchange") String exchange,
                              @RequestParam("key") String key,
                              @RequestBody Map<String, String> msg,
                              @RequestParam("delay") String delay); // 发送支付过期通知
}