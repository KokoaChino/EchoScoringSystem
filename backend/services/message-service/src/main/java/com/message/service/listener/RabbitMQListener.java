package com.message.service.listener;

import com.common.entity.Account;
import com.message.client.feign.AuthClient;
import com.message.mapper.MqMapper;
import com.message.service.api.MessageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;
import java.util.Map;


@Slf4j
@Component
public class RabbitMQListener {

    @Resource
    AuthClient authClient;

    @Resource
    MessageService messageService;

    @Resource
    MqMapper mqMapper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q4", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
            exchange = @Exchange(name = "e1"),
            key = {"code"}
    ))
    public void listenCodeMessage(Map<String, String> msg) { // 发送验证码邮件
        System.out.println("发送验证码邮件" + msg);
        String id = msg.get("id"), email = msg.get("email"), code = msg.get("code");
        Integer status = mqMapper.selectMessageIdLog(id);
        if (status != null && status != 0) return;
        Boolean res = messageService.sendCodeEmail(email, code);
        if (res) mqMapper.updateMessageIdLog(id, 1);
        else {
            mqMapper.updateMessageIdLog(id, 2);
            throw new RuntimeException("发送邮件失败");
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q1", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
            exchange = @Exchange(name = "e1"),
            key = {"pay_success"}
    ))
    public void listenPaySuccessMessage(Map<String, String> msg) { // 支付成功通知
        String id = msg.get("id"), username = msg.get("username");
        Integer status = mqMapper.selectMessageIdLog(id);
        if (status != null && status != 0) return;
        System.out.println("account" + "///////////////////////////////////////////////////////////////////////////////");
        Account account = authClient.getAccount(username);
        String email = account.getEmail();
        Boolean res = messageService.sendPaySuccessEmail(email, username);
        if (res) mqMapper.updateMessageIdLog(id, 1);
        else {
            mqMapper.updateMessageIdLog(id, 2);
            throw new RuntimeException("发送邮件失败");
        }
    }

    @RabbitListener(queues = "q3")
    public void listenPayFailedMessage(Map<String, String> msg) { // 订单过期通知
        String id = msg.get("id"), outTradeNo = msg.get("outTradeNo"), username = msg.get("username"), time = msg.get("time");
        Integer status = mqMapper.selectMessageIdLog(id);
        if (status != null && status != 0) return;
        if (authClient.isVip(username)) { // 用户已支付
            mqMapper.updateMessageIdLog(id, 1);
            return;
        }
        Account account = authClient.getAccount(username);
        String email = account.getEmail();
        Boolean res = messageService.sendPayFailedEmail(email, username, time, outTradeNo);
        if (res) mqMapper.updateMessageIdLog(id, 1);
        else {
            mqMapper.updateMessageIdLog(id, 2);
            throw new RuntimeException("发送邮件失败");
        }
    }
}