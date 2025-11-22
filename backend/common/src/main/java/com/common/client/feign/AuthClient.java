package com.common.client.feign;

import com.common.client.fallback.AuthClientFallback;
import com.common.entity.AuthenticationDTO;
import com.common.entity.RestBean;
import com.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "auth-service",
             path = "/api/auth",
             fallback = AuthClientFallback.class)
public interface AuthClient {

    @PostMapping("/get-user")
    RestBean<User> getUser(@RequestBody AuthenticationDTO dto);

    @PostMapping("/update-vip-user")
    RestBean<Void> updateVipUser(@RequestBody AuthenticationDTO dto);
}