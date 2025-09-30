package com.youthconnect.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全配置
 */
@Configuration
public class SecurityConfig {

    /**
     * 密码编码器
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 关闭 CSRF、防止拦截，允许所有请求
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

}
