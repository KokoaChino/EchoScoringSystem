package com.example.service.impl;

import com.example.entity.auth.AccountUser;
import com.example.entity.common.RestBean;
import com.example.mapper.UserMapper;
import com.example.service.api.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public RestBean<AccountUser> me(AccountUser user) { // 记住我功能
        return RestBean.success(user);
    }

    @Override
    public boolean isVip(String username) { // 查询用户是否为 VIP 用户
        return userMapper.isVipUser(username);
    }
}