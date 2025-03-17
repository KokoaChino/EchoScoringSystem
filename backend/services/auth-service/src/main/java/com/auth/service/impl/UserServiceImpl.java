package com.auth.service.impl;

import com.auth.service.api.UserService;
import com.common.entity.AccountUser;
import com.common.entity.RestBean;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Override
    public RestBean<AccountUser> me(AccountUser user) { // 记住我功能
        return RestBean.success(user);
    }
}