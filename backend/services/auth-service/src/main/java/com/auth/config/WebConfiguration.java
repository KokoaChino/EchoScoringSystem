package com.auth.config;

import com.auth.interceptor.AuthorizeInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfiguration implements WebMvcConfigurer { // MVC 配置类

    @Resource
    AuthorizeInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) { // 配置自定义拦截器
        registry // 注册拦截器
                .addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**")
                .excludePathPatterns("/pay/notify");
    }
}