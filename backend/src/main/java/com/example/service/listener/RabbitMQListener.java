package com.example.service.listener;

import com.example.entity.auth.Account;
import com.example.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;


@Component
public class RabbitMQListener {

    @Resource
    UserMapper userMapper;

    @Value("${spring.mail.username}")
    String from;

    @Resource
    JavaMailSender mailSender;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q1", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
            exchange = @Exchange(name = "e1"),
            key = {"pay_success"}
    ))
    public void listenPaysuccessMessage(String username) {
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
    -- 星开祈灵 ξ( ✿＞◡❛)
    """, username);
            helper.setText(emailContent);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}