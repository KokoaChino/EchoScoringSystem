package com.example.controller;

import com.example.entity.common.RestBean;
import com.example.entity.auth.AccountUser;
import com.example.service.api.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController { // 与用户信息相关的控制器

    @Resource
    UserService service;

    @GetMapping("/me")
    public RestBean<AccountUser> me(@SessionAttribute("account") AccountUser user) { // 记住我功能
        return service.me(user);
    }

    @PostMapping("/is-vip")
    public boolean isVip(@RequestParam("username") String username) { // 查询用户是否为 VIP 用户
        return service.isVip(username);
    }
}