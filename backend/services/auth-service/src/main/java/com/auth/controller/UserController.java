package com.auth.controller;

import com.auth.service.api.UserService;
import com.common.entity.AccountUser;
import com.common.entity.RestBean;
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
}