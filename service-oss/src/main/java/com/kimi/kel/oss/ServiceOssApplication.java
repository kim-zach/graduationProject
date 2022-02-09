package com.kimi.kel.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.kimi.kel","com.kimi.common"})
@EnableFeignClients
public class ServiceOssApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(ServiceOssApplication.class,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
