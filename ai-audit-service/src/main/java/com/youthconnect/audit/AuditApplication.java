package com.youthconnect.audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * AI审核服务启动类
 * 
 * @author YouthConnect
 * @since 2024-01-01
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuditApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuditApplication.class, args);
    }
}
