package com.example.service.listener;

import com.example.entity.auth.Account;
import com.example.mapper.MqMapper;
import com.example.mapper.PayMapper;
import com.example.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;
import java.util.Map;


@Slf4j
@Component
public class RabbitMQListener {

    @Resource
    UserMapper userMapper;

    @Resource
    PayMapper payMapper;

    @Resource
    MqMapper mqMapper;

    @Value("${spring.mail.username}")
    String from;

    @Resource
    JavaMailSender mailSender;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q1", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
            exchange = @Exchange(name = "e1"),
            key = {"pay_success"}
    ))
    public void listenPaySuccessMessage(Map<String, String> msg) { // 支付成功通知
        String id = msg.get("id"), outTradeNo = msg.get("outTradeNo"), username = msg.get("username");
        Integer status = mqMapper.selectMessageIdLog(id);
        if (status != null && status != 0) return;
        Account account = userMapper.findAccountByNameOrEmail(username);
        String email = account.getEmail();
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
            mqMapper.updateMessageIdLog(id, 1);
        } catch (MessagingException | UnsupportedEncodingException e) {
            mqMapper.updateMessageIdLog(id, 2);
            throw new RuntimeException("发送邮件失败", e);
        }
    }

    @RabbitListener(queues = "q3")
    public void listenPayFailedMessage(Map<String, String> msg) { // 订单过期通知
        String id = msg.get("id"), outTradeNo = msg.get("outTradeNo"), username = msg.get("username"), time = msg.get("time");
        Integer status = mqMapper.selectMessageIdLog(id);
        if (status != null && status != 0) return;
        if (payMapper.isVipUser(username)) { // 用户已支付
            mqMapper.updateMessageIdLog(id, 1);
            return;
        }
        Account account = userMapper.findAccountByNameOrEmail(username);
        String email = account.getEmail();
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
            mqMapper.updateMessageIdLog(id, 1);
        } catch (MessagingException | UnsupportedEncodingException e) {
            mqMapper.updateMessageIdLog(id, 2);
            throw new RuntimeException("发送邮件失败", e);
        }
    }
}