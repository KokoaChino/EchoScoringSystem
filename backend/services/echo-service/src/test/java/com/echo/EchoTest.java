package com.echo;

import com.common.constant.Constant;
import com.common.mapper.DataGovernanceMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class EchoTest {

    @Resource
    private DataGovernanceMapper mapper;

    @Test
    public void test() {
        mapper.findAllTables(Constant.ECHO_SERVICE_DB).forEach(System.out::println);
    }
}
