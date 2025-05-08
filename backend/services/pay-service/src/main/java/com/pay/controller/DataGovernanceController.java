package com.pay.controller;

import com.pay.service.api.DataGovernanceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/pay")
public class DataGovernanceController {

    @Resource
    DataGovernanceService service;

    @PostMapping("/change-username")
    public void changeUsername(@RequestParam("username") String username,
                               @RequestParam("oldUsername") String oldUsername) { // 重置名称
        service.changeUsername(username, oldUsername);
    }

    @PostMapping("/signout")
    public void signout(@RequestParam("username") String username) { // 注销用户
        service.signout(username);
    }
}