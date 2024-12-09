package com.example;

import com.example.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class StudyProjectBackendApplicationTests { // SpringBoot 测试框架

    @Resource
    UserMapper mapper;

    @Test
    void contextLoads() {
        System.out.println("测一测星开祈灵");
    }
}