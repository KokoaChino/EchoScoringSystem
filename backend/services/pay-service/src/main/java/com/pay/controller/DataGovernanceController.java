package com.pay.controller;

import com.common.constant.Constant;
import com.common.entity.AuthenticationDTO;
import com.common.service.api.DataGovernanceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/pay")
public class DataGovernanceController {

    @Resource
    private DataGovernanceService service;

    @PostMapping("/change-username")
    public void changeUsername(@RequestBody AuthenticationDTO dto) { // 重置名称
        dto.verify();
        service.changeUsername(Constant.PAY_SERVICE_DB,
                dto.getUsername(), (String) dto.getExtra());
    }

    @PostMapping("/signout")
    public void signout(@RequestBody AuthenticationDTO dto) { // 注销用户
        dto.verify();
        service.signout(Constant.PAY_SERVICE_DB, dto.getUsername());
    }
}