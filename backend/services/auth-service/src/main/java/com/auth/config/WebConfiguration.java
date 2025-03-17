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
                .addInterceptor(interceptor) // 添加 AuthorizeInterceptor 到拦截器注册表
                .addPathPatterns("/**") // 设置拦截器拦截所有路径
                .excludePathPatterns("/api/auth/**") // 排除路径下的请求不被拦截
                .excludePathPatterns("/pay/notify");
    }
}