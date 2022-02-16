package com.kimi.kel.core.config;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class OnlineCount implements HttpSessionListener {


    public int count = 0;


    @Override
    public void sessionCreated(HttpSessionEvent se) {
        count++;
        se.getSession().getServletContext().setAttribute("count",count);
        if(count == 3){
//            DealCards.dealCards(se);
            //发牌
            //TODO
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        count--;
        se.getSession().getServletContext().setAttribute("count",count);
    }
}
