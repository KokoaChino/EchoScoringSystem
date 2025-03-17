package com.pay.client.feign;

import com.common.entity.Account;
import com.pay.client.fallback.AuthClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "auth-service",
             path = "/api/auth",
             fallback = AuthClientFallback.class)
public interface AuthClient {

    @GetMapping("/get-account")
    Account getAccount(@RequestParam("username") String username); // 获取用户实体

    @GetMapping("/is-vip")
    Boolean isVip(@RequestParam("username") String username); // 查询用户是否为 VIP 用户

    @PostMapping("/update-vip-user")
    void updateVipUser(@RequestParam("username") String username); // 更新用户 VIP
}