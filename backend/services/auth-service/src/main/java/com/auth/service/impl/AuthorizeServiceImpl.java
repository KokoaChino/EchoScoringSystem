package com.auth.service.impl;

import com.auth.client.feign.EchoClient;
import com.auth.client.feign.MessageClient;
import com.auth.client.feign.PayClient;
import com.auth.mapper.UserMapper;
import com.auth.service.api.AuthorizeService;
import com.common.constant.Constant;
import com.common.context.UserContext;
import com.common.entity.AuthenticationDTO;
import com.common.entity.RestBean;
import com.common.entity.User;
import com.common.mapper.DataGovernanceMapper;
import jakarta.annotation.Resource;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private DataGovernanceMapper dataGovernanceMapper;

    @Resource
    private MessageClient messageClient;

    @Resource
    private EchoClient echoClient;

    @Resource
    private PayClient payClient;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Resource
    private JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String sendValidateEmail(String email, String sessionId, boolean hasAccount) { // 发送验证邮件
        String key = "email：" + sessionId + "：" + email + "：" + hasAccount;
        if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, "pending", 3, TimeUnit.MINUTES))) {
            String code = String.format("%06d", new Random().nextInt(1000000));
            redisTemplate.opsForValue().set(key, code, 3, TimeUnit.MINUTES);
            messageClient.sendCodeEmail(email, code);
            return null;
        } else {
            Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            if (expire > 120) {
                return "请求过于频繁，请稍后再试";
            }
            return "已有请求正在处理，请勿重复提交";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) { // 验证并注册
        String key = "email：" + sessionId + "：" + email + "：false";
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            String s = redisTemplate.opsForValue().get(key);
            if (s == null) return "验证码失效，请重新请求";
            if (s.equals(code)) {
                User user = userMapper.findUserByNameOrEmail(username);
                if (user != null) return "此用户名已被注册，请更换用户名";
                redisTemplate.delete(key);
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
        String key = "email：" + sessionId + "：" + email + "：true";
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            String s = redisTemplate.opsForValue().get(key);
            if (s == null) return "验证码失效，请重新请求";
            if (s.equals(code)) {
                redisTemplate.delete(key);
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
        String key = "email：" + sessionId + "：" + email + "：false";
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            String s = redisTemplate.opsForValue().get(key);
            if (s == null) return "验证码失效，请重新请求";
            if (s.equals(code)) {
                redisTemplate.delete(key);
                return null;
            } else {
                return "验证码错误，请检查后再提交";
            }
        } else {
            return "请先请求一封验证码邮件";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public boolean changeUsername(String username, String email) { // 重置名称
        String oldUsername = userMapper.findUserByNameOrEmail(email).getUsername();
        if (oldUsername == null) {
            throw new RuntimeException("原用户名不存在");
        }
        boolean res = userMapper.resetUsernameByEmail(username, email) > 0;
        for (String table : dataGovernanceMapper.findAllTables(Constant.AUTH_SERVICE_DB)) {
            dataGovernanceMapper.resetUsername(table, username, oldUsername);
        }
        AuthenticationDTO dto = new AuthenticationDTO();
        dto.setUsername(username);
        dto.setExtra(oldUsername);
        echoClient.changeUsername(dto);
        payClient.changeUsername(dto);
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
    public RestBean<?> login(User user) { // 用户登录
        try {
            User userDetails = userMapper.findUserByNameOrEmail(user.getUsername()); // 查用户详情
            if (userDetails == null) {
                return RestBean.failure(401, "用户不存在");
            }
            if (!encoder.matches(user.getPassword(), userDetails.getPassword())) { // 验证密码
                return RestBean.failure(401, "密码错误");
            }
            String token = jwtService.generateToken( // 生成 token
                    userDetails.getUsername(),
                    userDetails.getId(),
                    List.of("USER")
            );
            return RestBean.success(new HashMap<>(){{
                put("token", token);
                put("user", userDetails);
            }});
        } catch (Exception e) {
            return RestBean.failure(500, "系统异常：" + e.getMessage());
        }
    }

    @Override
    public RestBean<?> logout() { // 用户登出
        String username = UserContext.getUsername();
        if (username == null) {
            return RestBean.failure(401, "未登录");
        }
        String cacheKey = Constant.USER_CONTEXT_CACHE_KEY_PREFIX + username;
        redisTemplate.delete(cacheKey);
        return RestBean.success("退出登录成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public void signout(String username) { // 注销用户
        for (String tableName : dataGovernanceMapper.findAllTables(Constant.AUTH_SERVICE_DB)) {
            dataGovernanceMapper.deleteAccountByUsername(tableName, username);
        }
        AuthenticationDTO baseDTO = AuthenticationDTO.getBaseDTO(username);
        echoClient.signout(baseDTO);
        payClient.signout(baseDTO);
    }

    @Override
    public boolean checkUser(String username) { // 检查用户实体
        return userMapper.findUserByNameOrEmail(username) != null;
    }

    @Override
    public void updateLastVisited(String username) {
        userMapper.updateLastVisitedByUsername(username);
    }

    @Override
    public User getUser(String username) { // 获取用户实体
        return userMapper.findUserByNameOrEmail(username);
    }

    @Override
    public Boolean updateVipUser(String username, Map<String, String> msg) { // 更新用户 VIP
        int row = userMapper.updateVipUser(username);
        if (row > 0) {
            String cacheKey = Constant.USER_CONTEXT_CACHE_KEY_PREFIX + username;
            redisTemplate.delete(cacheKey);
            messageClient.sendMqMessage("e1", "pay_success", msg);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}