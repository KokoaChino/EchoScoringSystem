package com.auth.client.feign;

import com.common.entity.AuthenticationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "pay-service",
             path = "/api/pay")
public interface PayClient {

    @PostMapping("/change-username")
    void changeUsername(@RequestBody AuthenticationDTO dto); // 重置名称

    @PostMapping("/signout")
    void signout(@RequestBody AuthenticationDTO dto); // 注销用户
}