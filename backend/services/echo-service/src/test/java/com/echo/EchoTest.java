package com.echo;

import com.echo.mapper.DataGovernanceMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class EchoTest {

    @Resource
    DataGovernanceMapper mapper;

    @Test
    public void test() {
        mapper.findAllTables().forEach(System.out::println);
    }
}
