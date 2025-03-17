package com.message.controller;

import com.message.service.api.MessageService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/api/message")
public class MessageController { // 有关邮件消息的控制器

    @Resource
    MessageService messageService;

    @PostMapping("/send-code-email")
    public void sendCodeEmail(@RequestParam("email") String email,
                                @RequestParam("code") String code) { // 发送验证码邮件
        messageService.sendCodeMqMessage(email, code);
    }

    @PostMapping("/insert-message-id-log")
    public void insertMessageIdLog(@RequestParam("messageId") String messageId,
                                     @RequestParam("status") int status) { // 插入消息ID日志
        messageService.insertMessageIdLog(messageId, status);
    }

    @PostMapping("/send-mq-message")
    public void sendMqMessage(@RequestParam("exchange") String exchange,
                              @RequestParam("key") String key,
                              @RequestBody Map<String, String> msg) { // 发送支付成功通知
        messageService.sendMqMessage(exchange, key, msg);
    }

    @PostMapping("/send-delayed-mq-message")
    public void sendDelayedMqMessage(@RequestParam("exchange") String exchange,
                                     @RequestParam("key") String key,
                                     @RequestBody Map<String, String> msg,
                                     @RequestParam("delay") String delay) { // 发送支付过期通知
        messageService.sendDelayedMqMessage(exchange, key, msg, delay);
    }
}