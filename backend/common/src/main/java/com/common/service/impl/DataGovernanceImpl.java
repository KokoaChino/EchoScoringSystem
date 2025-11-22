package com.common.service.impl;

import com.common.mapper.DataGovernanceMapper;
import com.common.service.api.DataGovernanceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DataGovernanceImpl implements DataGovernanceService {

    @Resource
    private DataGovernanceMapper dataGovernanceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeUsername(String serviceDb, String username, String oldUsername) { // 重置名称
        for (String tableName : dataGovernanceMapper.findAllTables(serviceDb)) {
            dataGovernanceMapper.resetUsername(tableName, username, oldUsername);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signout(String serviceDb, String username) { // 注销用户
        for (String tableName : dataGovernanceMapper.findAllTables(serviceDb)) {
            dataGovernanceMapper.deleteAccountByUsername(tableName, username);
        }
    }
}