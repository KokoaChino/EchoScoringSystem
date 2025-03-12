package com.example;

import com.example.mapper.PayMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Slf4j
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
        String id = "114514";
        Map<String, String> msg = new HashMap<>(
                Map.ofEntries(
                        Map.entry("id", id + "-FAILED"),
                        Map.entry("outTradeNo", id),
                        Map.entry("username", username),
                        Map.entry("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                )
        );
        rabbitTemplate.convertAndSend("e1", "pay_failed", msg, message -> {
            message.getMessageProperties().setExpiration("5000");
            return message;
        });
    }
}