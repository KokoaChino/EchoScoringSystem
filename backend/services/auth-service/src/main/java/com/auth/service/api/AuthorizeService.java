package com.auth.service.api;

import com.common.entity.RestBean;
import com.common.entity.User;


public interface AuthorizeService {

    String sendValidateEmail(String email, String sessionId, boolean hasAccount); // 发送验证邮件
    String validateAndRegister(String username, String password, String email, String code, String sessionId); // 验证并注册
    String validateOnly(String email, String code, String sessionId); // 只验证
    String validateOnlyFalse(String email, String code, String sessionId); // 只验证

    boolean resetPassword(String password, String email); // 重置密码
    boolean changeUsername(String username, String email); // 重置名称
    boolean changeEmail(String oldEmail, String newEmail); // 重置邮箱

    RestBean<?> login(User user); // 用户登录
    RestBean<?> logout(); // 用户登出
    void signout(String username); // 注销用户

    boolean checkUser(String username); // 检查用户实体

    User getUser(String username); // 获取用户实体
    void updateVipUser(String username); // 更新用户 VIP
}