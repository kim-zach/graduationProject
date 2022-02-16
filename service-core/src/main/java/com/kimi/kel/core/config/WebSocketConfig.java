package com.kimi.kel.core.config;

import com.kimi.kel.core.pojo.entities.UserInfo;
import com.kimi.kel.core.service.UserInfoService;
import com.kimi.kel.core.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    private void setRedisService(UserInfoService userInfoService){

        WebSocketServer.userInfoService = userInfoService;

    }
}
