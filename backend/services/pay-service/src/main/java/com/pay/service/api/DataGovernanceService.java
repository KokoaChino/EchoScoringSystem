package com.pay.service.api;


public interface DataGovernanceService {
    void changeUsername(String username, String oldUsername); // 重置名称
    void signout(String username); // 注销用户
}