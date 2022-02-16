package com.kimi.kel.core.service;

import com.alibaba.fastjson.JSONObject;
import com.kimi.kel.core.utils.RulesCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@Service
@ServerEndpoint("/api/core/websocket/{sid}")
public class WebSocketServer {

    public static UserInfoService userInfoService;


    //当前在线连接数
    private static int onlineCount = 0;
    //存放每个客户端对应的MyWebSocket对象
//    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    /**
     * static
     * 储存链接 <sid, set<instance>>
     * ConcurrentHashMap 线程安全，读取非阻塞
     * CopyOnWriteArraySet 线程安全
     */
    private static final ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketServer>> webSocketSet = new ConcurrentHashMap<>();


    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送消息。
     * 每个用户（连接）私有。
     */
    private Session session;
    private String sid;
    private String userId;

    //接收sid
//    private String sid = "";

    /**
     * 连接建立成功调用的方法
     */
//    @OnOpen
//    public void onOpen(Session session, @PathParam("sid") String sid) {
//        this.session = session;
//        webSocketSet.add(this);     //加入set中
//        this.sid = sid;
//        addOnlineCount();           //在线数加1
//        try {
//            sendMessage("conn_success");
//            log.info("有新窗口开始监听:" + sid + ",当前在线人数为:" + getOnlineCount());
//        } catch (IOException e) {
//            log.error("websocket IO Exception");
//        }
//    }
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "sid") String sid ){
        this.session = session;
        this.sid  = sid;
//        this.userId = userId;
        log.info("userInfo:{}",userInfoService.getById(sid));
//        websocket先于spring bean容器加载
        //先查找是否有sid
        CopyOnWriteArraySet<WebSocketServer> users = webSocketSet.get(sid);
        if (users == null) {
            //处理读并发
            synchronized (webSocketSet) {
                if (!webSocketSet.contains(sid)) {
                    users = new CopyOnWriteArraySet<>();
                    webSocketSet.put(sid, users);
//                    generateWordGatherInfo(sid);
                }
            }
        }
        users.add(this);
        log.info("users:{}",users);
        log.info("连接成功，当前房间数为：" + webSocketSet.size()
                + "，连接ID：" + sid
                + "，房间号：" + sid
                + "，当前房间人数：" + webSocketSet.get(sid).size());
    }

    public void generateWordGatherInfo(String sid) {
        if(webSocketSet.get(sid).size() == 2){

        }
    }


    /**
     * 连接关闭调用的方法
     */
//    @OnClose
//    public void onClose() {
//        webSocketSet.remove(this);  //从set中删除
//        subOnlineCount();           //在线数减1
//
//        log.info("释放的sid为："+sid);
//
//        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
//
//    }
    @OnClose
    public void onClose(Session session) {
        // 避免多人同时在线直接关闭通道。
        Object[] objects = webSocketSet.get(this.sid).toArray();
        for (int i = 0; i < objects.length; i++) {
            if(((WebSocketServer) objects[i]).session.equals(session)){
                //删除房间中当前用户
                webSocketSet.get(this.sid).remove((WebSocketServer) objects[i]);
            }
        }
        if(webSocketSet.get(sid).size() <= 0){
            //删除房间
            webSocketSet.remove(sid);
            log.info("ID：" + sid+ "退出成功 ");
        }else{
            log.info("ID：" + sid+ " 1名用户退出，剩余" + webSocketSet.get(sid).size() + "名用户在线");
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * @ Param message 客户端发送过来的消息
     */
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        log.info("收到来自窗口" + sid + "的信息:" + message);
//        //群发消息
//        for (WebSocketServer item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    @OnMessage
    public void onMessage(String message) {
        log.info("收到消息：" + message);
        //刷新实时巡检进度
        CopyOnWriteArraySet<WebSocketServer> users = webSocketSet.get(sid);
        if (users != null) {
            for (WebSocketServer user : users) {
                user.session.getAsyncRemote().sendText(message);
                log.info("发送消息：" + message);
            }
        }
    }

    /**
     * @ Param session
     * @ Param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);

        CopyOnWriteArraySet<WebSocketServer> users = webSocketSet.get(sid);


        for (WebSocketServer item : users) {
            try {
                //为null则全部推送
                if (sid == null) {
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

//    public void sendInfo(String sid, Integer userId, Integer onlineSum, String info) {
        // 获取该连接用户信息
//        User currentUser = ApplicationContextUtil.getApplicationContext().getBean(UserMapper.class).selectById(userId);

        // 发送通知
//        MsgVO msg = new MsgVO();
//        msg.setCount(onlineSum);
//        msg.setUserId(userId);
//        msg.setAvatar(currentUser.getAvatar());
//        msg.setMsg(currentUser.getNickName() + info);
//        // json对象转字符串
//        String text = JSONObject.toJSONString(msg);
//        onMessage(sid, userId, text);
//    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

//    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet() {
//        return webSocketSet;
//    }
}
