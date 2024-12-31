package com.example.service.impl;

import com.example.entity.auth.Account;
import com.example.mapper.UserMapper;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class AuthorizeServiceImpl implements AuthorizeService { // 用户授权服务

    @Resource
    UserMapper mapper;

    @Value("${spring.mail.username}")
    String from; // 邮件发送者的邮箱地址

    @Resource
    JavaMailSender mailSender; // 邮件发送组件

    @Resource
    StringRedisTemplate template; // Redis 字符串模板，用于存取数据

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // 用于密码加密

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 根据用户名加载用户信息
        if (username == null)
            throw new UsernameNotFoundException("用户名不能为空");
        Account account = mapper.findAccountByNameOrEmail(username);
        if (account == null)
            throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    @Override
    public String sendValidateEmail(String email, String sessionId, boolean hasAccount) { // 发送验证邮件
        String key = "email:" + sessionId + ":" + email + ":" + hasAccount; // 操作 Redis 的 key，用来唯一标识邮件请求
        if (Boolean.TRUE.equals(template.hasKey(key))) { // 检查重发请求的频率
            Long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L); // 获取 key 的过期时间
            if (expire > 120) return "请求频繁，请稍后再试";
        }
        Account account = mapper.findAccountByNameOrEmail(email);
        if (hasAccount && account == null) return "没有此邮件地址的账户";
        if (!hasAccount && account != null) return "此邮箱已被其他用户注册";
        Random random = new Random();
        String code = String.format("%06d", random.nextInt(1000000)); // 创建验证码
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(from, "声骸评分系统");
            helper.setTo(email);
            helper.setSubject("【声骸评分系统的验证邮件】");
            helper.setText("您的验证码是：" + code);
            mailSender.send(message);
            template.opsForValue().set(key, code, 3, TimeUnit.MINUTES); // 在 Redis 中保存验证码，有效期为 3 分钟
            return null;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "邮件发送失败，请检查邮件地址是否有效";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) { // 验证并注册
        String key = "email:" + sessionId + ":" + email + ":false"; // 创建一个唯一的键，用于存储和验证该会话中的电子邮件验证码
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key); // 从 Redis 获取与该键相关联的验证码
            if (s == null) return "验证码失效，请重新请求"; // 如果获取的验证码为空，则提示用户验证码已失效
            if (s.equals(code)) {
                Account account = mapper.findAccountByNameOrEmail(username);
                if (account != null) return "此用户名已被注册，请更换用户名";
                template.delete(key); // 验证成功后，从 Redis 中删除验证码，确保验证码只能使用一次
                password = encoder.encode(password);
                if (mapper.createAccount(username, password, email) > 0) {
                    return null;
                } else {
                    return "创建用户失败，请联系作者：'星开祈灵'";
                }
            } else {
                return "验证码错误，请检查后再提交";
            }
        } else {
            return "请先请求一封验证码邮件";
        }
    }

    @Override
    public String validateOnly(String email, String code, String sessionId) { // 只验证
        String key = "email:" + sessionId + ":" + email + ":true"; // 创建一个唯一的键，用于验证仅验证码，不涉及账户注册
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key); // 从 Redis 中获取与该键关联的验证码
            if (s == null) return "验证码失效，请重新请求";
            if (s.equals(code)) {
                template.delete(key); // 验证成功后，从 Redis 中删除验证码，避免重复使用
                return null;
            } else {
                return "验证码错误，请检查后再提交";
            }
        } else {
            return "请先请求一封验证码邮件";
        }
    }

    @Override
    public String validateOnlyFalse(String email, String code, String sessionId) { // 只验证
        String key = "email:" + sessionId + ":" + email + ":false"; // 创建一个唯一的键，用于验证仅验证码，不涉及账户注册
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key); // 从 Redis 中获取与该键关联的验证码
            if (s == null) return "验证码失效，请重新请求";
            if (s.equals(code)) {
                template.delete(key); // 验证成功后，从 Redis 中删除验证码，避免重复使用
                return null;
            } else {
                return "验证码错误，请检查后再提交";
            }
        } else {
            return "请先请求一封验证码邮件";
        }
    }

    @Override
    public boolean changeUsername(String username, String oldUsername, String email) { // 重置名称
        boolean res = mapper.resetUsernameByEmail(username, email) > 0;
        for (String table : mapper.findAllTables()) {
            mapper.resetUsername(table, username, oldUsername);
        }
        return res;
    }

    @Override
    public boolean resetPassword(String password, String email) { // 重置密码
        password = encoder.encode(password);
        return mapper.resetPasswordByEmail(password, email) > 0;
    }

    @Override
    public boolean changeEmail(String oldEmail, String newEmail) { // 重置邮件
        return mapper.resetEmailByEmail(oldEmail, newEmail) > 0;
    }
}