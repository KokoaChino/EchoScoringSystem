package com.message.service.api;

import java.util.Map;


public interface MessageService {
    Boolean sendCodeEmail(String email, String code); // 发送验证码邮件
    Boolean sendPaySuccessEmail(String email, String username); // 发送支付成功邮件
    Boolean sendPayFailedEmail(String email, String username, String time, String outTradeNo); // 发送支付过期邮件

    void insertMessageIdLog(String messageId, int status); // 插入消息ID日志
    void sendCodeMqMessage(String email, String code); // 异步发送验证码邮件
    void sendMqMessage(String exchange, String key, Map<String, String> msg); // 发送支付成功通知
    void sendDelayedMqMessage(String exchange, String key, Map<String, String> msg, String delay); // 发送支付过期通知
}