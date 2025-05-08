package com.auth.interceptor;

import com.auth.mapper.UserMapper;
import com.common.entity.AccountUser;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class AuthorizeInterceptor implements HandlerInterceptor { // 自定义请求处理拦截器

    @Resource
    UserMapper mapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            String username = user.getUsername();
            AccountUser account = mapper.findAccountUserByNameOrEmail(username);
            request.getSession().setAttribute("account", account);
        }
        return true;
    }
}