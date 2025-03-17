package com.message;

import com.message.service.api.MessageService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class MessageTest {

    @Resource
    MessageService messageService;

    private final String email = "2178740980@qq.com";
    private final String code = "114514";

    @Test
    public void test() {
        messageService.sendCodeMqMessage(email, code);
    }
}
