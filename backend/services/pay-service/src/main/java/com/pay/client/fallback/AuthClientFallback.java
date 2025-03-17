package com.pay.client.fallback;

import com.common.entity.Account;
import com.pay.client.feign.AuthClient;
import org.springframework.stereotype.Component;


@Component
public class AuthClientFallback implements AuthClient {

    @Override
    public Account getAccount(String username) {
        return new Account();
    }

    @Override
    public Boolean isVip(String username) {
        return Boolean.FALSE;
    }

    @Override
    public void updateVipUser(String username) {

    }
}