package com.auth.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "echo-service",
             path = "/api/echo")
public interface EchoClient {

    @PostMapping("/change-username")
    void changeUsername(@RequestParam("username") String username,
                        @RequestParam("oldUsername") String oldUsername); // 重置名称

    @PostMapping("/signout")
    void signout(@RequestParam("username") String username); // 注销用户
}