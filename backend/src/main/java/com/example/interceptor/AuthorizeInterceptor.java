package com.example.interceptor;

import com.example.entity.user.AccountUser;
import com.example.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class AuthorizeInterceptor implements HandlerInterceptor { // 自定义请求处理拦截器

    @Resource
    UserMapper mapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) { // 处理请求前的逻辑
        SecurityContext context = SecurityContextHolder.getContext(); // 获取当前线程的安全上下文
        Authentication authentication = context.getAuthentication(); // 从安全上下文中获取认证信息
        User user = (User) authentication.getPrincipal(); // 获取认证用户的主体信息
        String username = user.getUsername(); // 获取当前用户的用户名
        AccountUser account = mapper.findAccountUserByNameOrEmail(username); // 使用用户名从数据库中查找 AccountUser 对象
        request.getSession().setAttribute("account", account); // 将找到的 AccountUser 对象存储到会话中，属性名为 "account"
        return true; // 继续请求处理流程
    }
}