package com.message.service.listener;

import com.common.client.feign.AuthClient;
import com.common.entity.AuthenticationDTO;
import com.common.entity.User;
import com.message.enums.MessageStatusEnum;
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
    private AuthClient authClient;

    @Resource
    private MessageService messageService;

    @Resource
    private MqMapper mqMapper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q4", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
            exchange = @Exchange(name = "e1"),
            key = {"code"}
    ))
    public void listenCodeMessage(Map<String, String> msg) { // 发送验证码邮件
        String id = msg.get("id"), email = msg.get("email"), code = msg.get("code");
        int inserted = mqMapper.insertMessageIdLog(id, MessageStatusEnum.PENDING.getCode());
        if (inserted == 0) return;
        Boolean res = messageService.sendCodeEmail(email, code);
        if (res) mqMapper.updateMessageIdLog(id, MessageStatusEnum.SUCCESS.getCode());
        else {
            mqMapper.updateMessageIdLog(id, MessageStatusEnum.FAILED.getCode());
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
        int inserted = mqMapper.insertMessageIdLog(id, MessageStatusEnum.PENDING.getCode());
        if (inserted == 0) return;
        User user = authClient.getUser(AuthenticationDTO.getBaseDTO(username)).getData();
        String email = user.getEmail();
        Boolean res = messageService.sendPaySuccessEmail(email, username);
        if (res) mqMapper.updateMessageIdLog(id, MessageStatusEnum.SUCCESS.getCode());
        else {
            mqMapper.updateMessageIdLog(id, MessageStatusEnum.FAILED.getCode());
            throw new RuntimeException("发送邮件失败");
        }
    }

    @RabbitListener(queues = "q3")
    public void listenPayFailedMessage(Map<String, String> msg) { // 订单过期通知
        String id = msg.get("id"), outTradeNo = msg.get("outTradeNo"), username = msg.get("username"), time = msg.get("time");
        int inserted = mqMapper.insertMessageIdLog(id, MessageStatusEnum.PENDING.getCode());
        if (inserted == 0) return;
        User user = authClient.getUser(AuthenticationDTO.getBaseDTO(username)).getData();
        if (user.getVip()) { // 用户已支付
            mqMapper.updateMessageIdLog(id, MessageStatusEnum.SUCCESS.getCode());
            return;
        }
        String email = user.getEmail();
        String pureOrderNo = outTradeNo.substring(outTradeNo.lastIndexOf('-') + 1);
        Boolean res = messageService.sendPayFailedEmail(email, username, time, pureOrderNo);
        if (res) mqMapper.updateMessageIdLog(id, MessageStatusEnum.SUCCESS.getCode());
        else {
            mqMapper.updateMessageIdLog(id, MessageStatusEnum.FAILED.getCode());
            throw new RuntimeException("发送邮件失败");
        }
    }
}