package com.auth.service.impl;

import com.auth.client.feign.EchoClient;
import com.auth.client.feign.MessageClient;
import com.auth.client.feign.PayClient;
import com.auth.mapper.DataGovernanceMapper;
import com.auth.mapper.UserMapper;
import com.auth.service.api.AuthorizeService;
import com.common.entity.Account;
import jakarta.annotation.Resource;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Resource
    UserMapper userMapper;

    @Resource
    DataGovernanceMapper dataGovernanceMapper;

    @Resource
    MessageClient messageClient;

    @Resource
    EchoClient echoClient;

    @Resource
    PayClient payClient;

    @Resource
    StringRedisTemplate template;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 根据用户名加载用户信息
        if (username == null)
            throw new UsernameNotFoundException("用户名不能为空");
        Account account = userMapper.findAccountByNameOrEmail(username);
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
        String key = "email:" + sessionId + ":" + email + ":" + hasAccount;
        if (Boolean.TRUE.equals(template.opsForValue().setIfAbsent(key, "pending", 3, TimeUnit.MINUTES))) {
            String code = String.format("%06d", new Random().nextInt(1000000));
            template.opsForValue().set(key, code, 3, TimeUnit.MINUTES);
            messageClient.sendCodeEmail(email, code);
            return null;
        } else {
            Long expire = template.getExpire(key, TimeUnit.SECONDS);
            if (expire != null && expire > 120) {
                return "请求过于频繁，请稍后再试";
            }
            return "已有请求正在处理，请勿重复提交";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) { // 验证并注册
        String key = "email:" + sessionId + ":" + email + ":false";
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key);
            if (s == null) return "验证码失效，请重新请求";
            if (s.equals(code)) {
                Account account = userMapper.findAccountByNameOrEmail(username);
                if (account != null) return "此用户名已被注册，请更换用户名";
                template.delete(key);
                password = encoder.encode(password);
                if (userMapper.createAccount(username, password, email) > 0) {
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
        String key = "email:" + sessionId + ":" + email + ":true";
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key);
            if (s == null) return "验证码失效，请重新请求";
            if (s.equals(code)) {
                template.delete(key);
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
        String key = "email:" + sessionId + ":" + email + ":false";
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            String s = template.opsForValue().get(key);
            if (s == null) return "验证码失效，请重新请求";
            if (s.equals(code)) {
                template.delete(key);
                return null;
            } else {
                return "验证码错误，请检查后再提交";
            }
        } else {
            return "请先请求一封验证码邮件";
        }
    }

    @Override
    @Transactional
    @GlobalTransactional
    public boolean changeUsername(String username, String email) { // 重置名称
        String oldUsername = userMapper.findAccountByNameOrEmail(email).getUsername();
        if (oldUsername == null) {
            throw new RuntimeException("原用户名不存在");
        }
        boolean res = userMapper.resetUsernameByEmail(username, email) > 0;
        for (String table : dataGovernanceMapper.findAllTables()) {
            dataGovernanceMapper.resetUsername(table, username, oldUsername);
        }
        echoClient.changeUsername(username, oldUsername);
        payClient.changeUsername(username, oldUsername);
        return res;
    }

    @Override
    public boolean resetPassword(String password, String email) { // 重置密码
        password = encoder.encode(password);
        return userMapper.resetPasswordByEmail(password, email) > 0;
    }

    @Override
    public boolean changeEmail(String oldEmail, String newEmail) { // 重置邮件
        return userMapper.resetEmailByEmail(oldEmail, newEmail) > 0;
    }

    @Override
    @Transactional
    @GlobalTransactional
    public void signout(String username) { // 注销用户
        for (String tableName : dataGovernanceMapper.findAllTables()) {
            dataGovernanceMapper.deleteAccountByUsername(tableName, username);
        }
        echoClient.signout(username);
        payClient.signout(username);
    }

    @Override
    public Boolean isVip(String username) { // 查询用户是否为 VIP 用户
        return userMapper.isVipUser(username);
    }

    @Override
    public Account getAccount(String username) { // 获取用户实体
        return userMapper.findAccountByNameOrEmail(username);
    }

    @Override
    public void updateVipUser(String username) { // 更新用户 VIP
        userMapper.updateVipUser(username);
    }
}