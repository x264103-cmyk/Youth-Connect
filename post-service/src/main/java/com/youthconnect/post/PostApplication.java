package com.youthconnect.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 动态服务启动类
 * 
 * @author YouthConnect
 * @since 2024-01-01
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PostApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostApplication.class, args);
    }
}
