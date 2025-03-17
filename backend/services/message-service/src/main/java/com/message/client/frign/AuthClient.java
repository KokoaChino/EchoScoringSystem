package com.message.client.frign;

import com.common.entity.Account;
import com.message.client.fallback.AuthClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "auth-service",
             path = "/api/auth",
             fallback = AuthClientFallback.class)
public interface AuthClient {

    @GetMapping("/get-account")
    Account getAccount(@RequestParam("username") String username); // 获取用户实体

    @GetMapping("/is-vip")
    Boolean isVip(@RequestParam("username") String username); // 查询用户是否为 VIP 用户
}