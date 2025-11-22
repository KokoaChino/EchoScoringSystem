package com.common.client.fallback;

import com.common.client.feign.AuthClient;
import com.common.entity.AuthenticationDTO;
import com.common.entity.RestBean;
import com.common.entity.User;
import org.springframework.stereotype.Component;


@Component
public class AuthClientFallback implements AuthClient {

    @Override
    public RestBean<User> getUser(AuthenticationDTO dto) {
        return RestBean.success(new User(0, "", dto.getUsername(), "", Boolean.FALSE));
    }

    @Override
    public RestBean<Void> updateVipUser(AuthenticationDTO dto) {
        return RestBean.failure(500);
    }
}