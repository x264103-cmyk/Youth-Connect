package com.youthconnect.recommend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 推荐服务启动类
 * 
 * @author YouthConnect
 * @since 2024-01-01
 */
@SpringBootApplication
@EnableDiscoveryClient
public class RecommendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendApplication.class, args);
    }
}
