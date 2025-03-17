package com.auth.service.api;

import com.common.entity.AccountUser;
import com.common.entity.RestBean;


public interface UserService {
    RestBean<AccountUser> me(AccountUser user); // 记住我功能
}