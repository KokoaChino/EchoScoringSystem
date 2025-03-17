package com.message.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitConfiguration {

    @Bean
    public MessageConverter messageConverter() { // 配置消息转换器
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setCreateMessageIds(true);
        return converter;
    }

    @Bean
    public DirectExchange e1() {
        return new DirectExchange("e1");
    }
    @Bean
    public DirectExchange e2() {
        return new DirectExchange("e2");
    }
    @Bean
    public Queue q2() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-queue-mode", "lazy");
        args.put("x-dead-letter-exchange", "e2");
        args.put("x-dead-letter-routing-key", "pay_failed.ttl");
        return new Queue("q2", true, false, false, args);
    }
    @Bean
    public Queue q3() {
        return new Queue("q3", true);
    }
    @Bean
    public Binding q2Binding(DirectExchange e1, Queue q2) {
        return BindingBuilder.bind(q2).to(e1).with("pay_failed");
    }
    @Bean
    public Binding q3Binding(DirectExchange e2, Queue q3) {
        return BindingBuilder.bind(q3).to(e2).with("pay_failed.ttl");
    }
}