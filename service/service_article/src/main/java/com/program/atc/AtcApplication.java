package com.program.atc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.program"})
@MapperScan("com.program.atc.mapper")
@EnableDiscoveryClient
@EnableFeignClients
public class AtcApplication {
    public static void main(String[] args) {
        SpringApplication.run(AtcApplication.class, args);
    }

}
