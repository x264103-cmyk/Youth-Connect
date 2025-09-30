package com.youthconnect.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayCorsConfig {

   @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // 允许携带 Cookie
        // 允许多个前端地址
        config.addAllowedOriginPattern("http://localhost:3000");
        config.addAllowedOriginPattern("http://localhost:3001");
        config.addAllowedOriginPattern("http://localhost:8080");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 全局生效

        return new CorsWebFilter(source);
    }
}
