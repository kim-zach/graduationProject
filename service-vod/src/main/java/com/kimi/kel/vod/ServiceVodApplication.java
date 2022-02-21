package com.kimi.kel.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan({"com.kimi.kel","com.kimi.common"})
@EnableFeignClients
public class ServiceVodApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceVodApplication.class,args);
    }
}
