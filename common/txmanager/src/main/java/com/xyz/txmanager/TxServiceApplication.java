package com.xyz.txmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients // 要使用Feign，需要加上此注解
public class TxServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TxServiceApplication.class, args);
    }
}
