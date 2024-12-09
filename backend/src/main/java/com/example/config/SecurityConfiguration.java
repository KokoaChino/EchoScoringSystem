package com.example.config;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.RestBean;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.sql.DataSource;
import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration { // Security 配置类

    @Resource
    AuthorizeService authorizeService; // 授权服务实例，用于用户授权操作

    @Resource
    DataSource dataSource; // 数据源实例，用于数据库连接

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           PersistentTokenRepository repository) throws Exception { // 安全过滤链
        return http
                .authorizeHttpRequests(e -> e // 配置请求授权规则
                        .requestMatchers("/api/auth/**").permitAll() // 允许所有用户访问以 /api/auth/ 开头的请求
                        .anyRequest().authenticated() // 其他请求需认证后访问
                )
                .formLogin(e -> e // 配置表单登录设置
                        .loginProcessingUrl("/api/auth/login") // 设置登录处理的 URL
                        .successHandler(this::onAuthenticationSuccess) // 登录成功后的处理逻辑
                        .failureHandler(this::onAuthenticationFailure) // 登录失败后的处理逻辑
                )
                .logout(e -> e // 配置注销设置
                        .logoutUrl("/api/auth/logout") // 设置注销请求的 URL
                        .logoutSuccessHandler(this::onAuthenticationSuccess) // 注销成功后的处理逻辑
                )
                .rememberMe(e -> e // 配置记住我功能
                        .rememberMeParameter("remember") // 记住我参数的名称
                        .tokenRepository(repository) // 设置 token 存储库
                        .tokenValiditySeconds(3600 * 24 * 7) // 设置 token 有效期为 7 天
                )
                .csrf(AbstractHttpConfigurer::disable) // 禁用 CSRF 防护机制
                .cors(e -> e.configurationSource(this.corsConfigurationSource())) // 配置 CORS 策略
                .exceptionHandling(e -> e.authenticationEntryPoint(this::onAuthenticationFailure)) // 配置异常处理
                .build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource); // 设置数据源
        jdbcTokenRepository.setCreateTableOnStartup(false); // 配置不在启动时创建表格
        return jdbcTokenRepository;
    }

    private CorsConfigurationSource corsConfigurationSource() { // 定义 CORS 配置源的方法
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOriginPattern("*"); // 允许所有的源进行跨域请求
        cors.setAllowCredentials(true); // 允许携带凭证
        cors.addAllowedHeader("*"); // 允许所有请求头
        cors.addAllowedMethod("*"); // 允许所有请求方法
        cors.addExposedHeader("*"); // 允许暴露所有响应头
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // 创建基于 URL 的 CORS 配置源
        source.registerCorsConfiguration("/**", cors); // 注册针对所有路径的 CORS 配置
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class); // 获取分享的 AuthenticationManagerBuilder 对象
        authenticationManagerBuilder.userDetailsService(authorizeService); // 设置用户详情服务
        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { // 提供 BCryptPasswordEncoder 用于密码加密
        return new BCryptPasswordEncoder();
    }

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException { // 当认证成功时调用的处理方法
        response.setCharacterEncoding("utf-8");
        if(request.getRequestURI().endsWith("/login"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
        else if(request.getRequestURI().endsWith("/logout"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("退出登录成功")));
    }

    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException { // 当认证失败时调用的处理方法
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, exception.getMessage())));
    }
}