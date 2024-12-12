package com.example.service;

import org.springframework.security.core.userdetails.UserDetailsService;


public interface AuthorizeService extends UserDetailsService { // 用户授权服务

    String sendValidateEmail(String email, String sessionId, boolean hasAccount); // 发送验证邮件

    String validateAndRegister(String username, String password, String email, String code, String sessionId); // 验证并注册

    String validateOnly(String email, String code, String sessionId); // 只验证

    String validateOnlyFalse(String email, String code, String sessionId); // 只验证

    boolean resetPassword(String password, String email); // 重置密码

    boolean changeUsername(String username, String oldUsername, String email); // 重置名称

    boolean changeEmail(String oldEmail, String newEmail); // 重置邮箱
}