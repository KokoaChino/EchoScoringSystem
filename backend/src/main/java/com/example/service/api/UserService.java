package com.example.service.api;

import com.example.entity.auth.AccountUser;
import com.example.entity.common.RestBean;


public interface UserService {
    RestBean<AccountUser> me(AccountUser user); // 记住我功能
    boolean isVip(String username); // 查询用户是否为 VIP 用户
}