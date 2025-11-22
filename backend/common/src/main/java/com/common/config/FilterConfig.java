package com.common.config;

import com.common.filter.UserContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<UserContextFilter> userContextFilterRegistration(UserContextFilter userContextFilter) {
        FilterRegistrationBean<UserContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(userContextFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}