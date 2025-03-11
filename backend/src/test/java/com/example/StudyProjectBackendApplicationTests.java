package com.example;

import com.example.mapper.PayMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;


@SpringBootTest
public class StudyProjectBackendApplicationTests {

    private final static String username = "星开祈灵";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    PayMapper payMapper;

    @Autowired
    StringRedisTemplate template;

    @Test
    public void testSimpleQueue() {
        for (int i = 0; i < 10; i++) {
            String message = "AAA";
            rabbitTemplate.convertAndSend("e1", "pay_success", message);
        }
    }

    @Test
    public void test() {
        System.out.println(Boolean.TRUE.equals(template.hasKey(username)));
        String json = template.opsForValue().get(username);
        System.out.println(json);
        System.out.println(Boolean.TRUE.equals(template.hasKey(username)));
    }
}