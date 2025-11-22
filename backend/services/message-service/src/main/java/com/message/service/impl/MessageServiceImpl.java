package com.message.service.impl;

import com.message.service.api.MessageService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public Boolean sendCodeEmail(String email, String code) { // 发送验证码邮件
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(from, "声骸评分系统");
            helper.setTo(email);
            helper.setSubject("【声骸评分系统】账户安全验证码（请及时查收）");
            String htmlContent = """
    <html>
    <body style='font-family: Microsoft YaHei, sans-serif;'>
        <p>尊敬的用户：</p>
        <p>您好！</p>
        <p>您正在申请【声骸评分系统】的账户安全验证服务，本次操作验证码为：<br>
        <strong style='color: #1890ff; font-size: 18px;'>%s</strong></p>
        
        <div style='color: #666; margin-top: 20px;'>
            <h4>【温馨提示】</h4>
            <ol>
                <li>本验证码3分钟内有效，请及时完成验证</li>
                <li>切勿向他人泄露验证码</li>
                <li>如非本人操作，请忽略本邮件</li>
            </ol>
        </div>
        
        <p>感谢您对声骸评分系统的信任与支持！(≧∀≦)ゞ<br>
        <hr style='border: 0; border-top: 1px solid #eee; margin: 20px 0;'>
        <p style='color: #999; font-size: 12px;'>此邮件为系统自动发送，请勿直接回复</p>
    </body>
    </html>
    """.formatted(code);
            helper.setText(htmlContent, true);
            mailSender.send(message);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean sendPaySuccessEmail(String email, String username) { // 发送支付成功邮件
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(from, "声骸评分系统");
            helper.setTo(email);
            helper.setSubject("\uD83C\uDF89 恭喜您成为声骸评分系统终身VIP会员！尊享特权已开启");
            String emailContent = String.format("""
    尊敬的%s：

    🌟 热烈欢迎您加入声骸评分系统终身VIP会员！

    您的会员身份将永久有效，未来所有新功能将为您免费开放！

    如有疑问，请联系您的专属顾问（微信：ryu0785）。

    再次感谢您的信任！
    星开祈灵
    ξ( ✿＞◡❛)
    """, username);
            helper.setText(emailContent);
            mailSender.send(message);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            return false;
        }
    }

    @Override
    public Boolean sendPayFailedEmail(String email, String username, String time, String outTradeNo) { // 发送支付过期邮件
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(from, "声骸评分系统");
            helper.setTo(email);
            helper.setSubject("您的VIP订单已过期，请重新获取支付二维码");
            String emailContent = String.format("""

    尊敬的%s：

    您的VIP订单已过期，请勿使用原二维码支付。请重新访问网站获取新的支付二维码完成会员购买。

    订单信息：
    - 订单创建时间：%s
    - 订单编号：%s

    感谢您的理解与支持！
    星开祈灵
    (´･ω･`)
    """, username, time, outTradeNo);
            helper.setText(emailContent);
            mailSender.send(message);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            return false;
        }
    }

    @Override
    public void sendCodeMqMessage(String email, String code) { // 异步发送验证码消息
        Map<String, String> msg = new HashMap<>(
                Map.ofEntries(
                        Map.entry("id", UUID.randomUUID() + "-CODE"),
                        Map.entry("email", email),
                        Map.entry("code", code)
                )
        );
        rabbitTemplate.convertAndSend("e1", "code", msg);
    }

    @Override
    public void sendMqMessage(String exchange, String key, Map<String, String> msg) { // 发送支付成功通知
        rabbitTemplate.convertAndSend(exchange, key, msg);
    }

    @Override
    public void sendDelayedMqMessage(String exchange, String key, Map<String, String> msg, String delay) { // 发送支付过期通知
        rabbitTemplate.convertAndSend(exchange, key, msg, message -> {
            message.getMessageProperties().setExpiration(delay);
            return message;
        });
    }
}