package com.kimi.kel.core.config;

import com.kimi.kel.core.pojo.entities.Player;
import com.kimi.kel.core.pojo.entities.WordGather;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class GetWordGatherTheTable implements ServletContextListener {

    public WordGather wordGather;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        wordGather = new WordGather();
        List<Player> players = new ArrayList<>();
        wordGather.setPlayerList(players);
        sce.getServletContext().setAttribute("wordGather", wordGather);
        System.out.println("启动");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("关闭");
    }
}
