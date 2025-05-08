package com.pay.service.impl;

import com.pay.mapper.DataGovernanceMapper;
import com.pay.service.api.DataGovernanceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DataGovernanceImpl implements DataGovernanceService {

    @Resource
    DataGovernanceMapper dataGovernanceMapper;

    @Override
    @Transactional
    public void changeUsername(String username, String oldUsername) { // 重置名称
        for (String table : dataGovernanceMapper.findAllTables()) {
            dataGovernanceMapper.resetUsername(table, username, oldUsername);
        }
    }

    @Override
    @Transactional
    public void signout(String username) { // 注销用户
        for (String tableName : dataGovernanceMapper.findAllTables()) {
            dataGovernanceMapper.deleteAccountByUsername(tableName, username);
        }
    }
}