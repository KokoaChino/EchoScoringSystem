package com.common.service.api;


public interface DataGovernanceService {
    void changeUsername(String serviceDb, String username, String oldUsername); // 重置名称
    void signout(String serviceDb, String username); // 注销用户
}