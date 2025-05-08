package com.auth.config;

import com.alibaba.fastjson.JSONObject;
import com.auth.service.api.AuthorizeService;
import com.common.entity.RestBean;
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
    AuthorizeService authorizeService;

    @Resource
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           PersistentTokenRepository repository) throws Exception { // 安全过滤链
        return http
                .authorizeHttpRequests(e -> e // 配置请求授权规则
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/pay/notify").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(e -> e // 配置表单登录设置
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure)
                )
                .logout(e -> e // 配置注销设置
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onAuthenticationSuccess)
                )
                .rememberMe(e -> e // 配置记住我功能
                        .rememberMeParameter("remember")
                        .tokenRepository(repository)
                        .tokenValiditySeconds(3600 * 24 * 15)
                )
                .csrf(AbstractHttpConfigurer::disable) // 禁用 CSRF 防护机制
                .exceptionHandling(e -> e.authenticationEntryPoint(this::onAuthenticationFailure)) // 配置异常处理
                .build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    private CorsConfigurationSource corsConfigurationSource() { // 定义 CORS 配置源的方法
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOriginPattern("*");
        cors.setAllowCredentials(true);
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(authorizeService);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException { // 当认证成功时调用的处理方法
        response.setCharacterEncoding("utf-8");
        if (request.getRequestURI().endsWith("/login"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
        else if (request.getRequestURI().endsWith("/logout"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("退出登录成功")));
    }

    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException { // 当认证失败时调用的处理方法
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, exception.getMessage())));
    }
}