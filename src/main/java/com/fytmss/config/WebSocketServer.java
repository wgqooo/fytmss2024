package com.fytmss.config;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wgq
 * @create 2024/8/12-周一 14:24
 * onopen (前端) 与 @OnOpen (后端): 当 WebSocket 连接成功建立时，前端的 onopen 事件处理函数会被触发，而后端的 @OnOpen 注解的方法会被调用。
 * onmessage (前端) 与 @OnMessage (后端): 当从服务器接收到消息时，前端的 onmessage 事件处理函数会被触发，而后端的 @OnMessage 注解的方法会处理这些消息。
 * onclose (前端) 与 @OnClose (后端): 当 WebSocket 连接关闭时，前端的 onclose 事件处理函数会被触发，而后端的 @OnClose 注解的方法会在连接关闭时被调用。
 * onerror (前端) 与 @OnError (后端): 当发生 WebSocket 错误时，前端的 onerror 事件处理函数会被触发，而后端的 @OnError 注解的方法会处理错误事件。
 */
@Getter
@Slf4j
@Component
@ServerEndpoint("/webSocket/{sessionId}")
public class WebSocketServer {

    /**
     * 静态变量，用来记录当前在线连接数
     * 我这里使用了ShiroSessionListener的sessionCount代替这里的count，因为同时维护两个表示【在线人数】的变量可能会有偏差
     */
    private static int count = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static final ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话
     */
    private Session session;
    /**
     * 接收sessionId
     */
    private String sessionId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sessionId") String sessionId) throws IOException {
        this.session = session;
        this.sessionId = sessionId;
        if (webSocketMap.containsKey(sessionId)) {
            webSocketMap.remove(sessionId);
            webSocketMap.put(sessionId, this);
        } else {
            webSocketMap.put(sessionId, this);
            addCount();
        }
        for (WebSocketServer webSocketServer : webSocketMap.values()) {
            //通知所有用户（当前的在线人数）
            sendInfo(String.valueOf(getConnectionCount()), webSocketServer.getSessionId());
        }
        log.info("websocket连接:" + sessionId+ ", 当前在线人数为:" + getConnectionCount());
    }

    /**
     *	 连接关闭调用的方法
     */
    @OnClose
    public void onClose() throws IOException {
        webSocketMap.remove(sessionId);
        subCount();
        for (WebSocketServer webSocketServer : webSocketMap.values()) {
            //通知所有用户（当前的在线人数）
            sendInfo(String.valueOf(getConnectionCount()), webSocketServer.getSessionId());
        }
        //但是当一个用户刷新页面的时候，session没有销毁，输出的会话数没变，websocket关了又开
        log.info("websocket关闭:" + sessionId+ ", 当前在线人数为:" + getConnectionCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //log.info(sessionId + "该用户上线" + message);
        //可以群发消息
        //消息可以保存到数据库、redis
        if (message != null) {
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromUserId", this.sessionId);
                String toUserId = jsonObject.getString("toUserId");
                //没有指定id，可以设置为群发
                if(toUserId == null){
                    for (WebSocketServer webSocketServer : webSocketMap.values()) {
                        //通知其他用户，该用户已上线
                        if(!Objects.equals(webSocketServer.getSessionId(), this.sessionId)){
                            sendInfo(jsonObject.getString("data"), webSocketServer.getSessionId());
                        }
                        //通知所有用户（当前的在线人数）
                        sendInfo(String.valueOf(getConnectionCount()), webSocketServer.getSessionId());
                    }
                    return;
                }
                //传送给对应toUserId用户的websocket
                if (webSocketMap.containsKey(toUserId)) {
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                } else {
                    //否则不在这个服务器上
                    log.info("请求的userId:" + toUserId + "不在该服务器上或不存在该地址");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.sessionId+ ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, @PathParam("sessionId") String sessionId) throws IOException {
        //log.info("发送消息到会话【" + sessionId+ "】，报文:" + message);
        if (sessionId != null && webSocketMap.containsKey(sessionId)) {
            webSocketMap.get(sessionId).sendMessage(message);
        } else {
            System.err.println("用户会话【" + sessionId + "】不在线！");
        }
    }

    /**
     * 群发消息
     * @return
     */
    public static void sendToAllUser(String msg, @PathParam("sessionId") String sessionId) throws IOException{
        //这里有个问题：当一个session创建时，调用了sendToAllUser()，但是此时onOpen()还没监听到websocket创建，自己看不见
        for (WebSocketServer webSocketServer : webSocketMap.values()) {
            //通知其他用户，该用户已上线
            if(!Objects.equals(webSocketServer.getSessionId(), sessionId)){
                sendInfo(msg, webSocketServer.getSessionId());
            }
            //通知所有用户（当前的在线人数）
            sendInfo(String.valueOf(getConnectionCount()), webSocketServer.getSessionId());
        }
        log.info("向所有用户发送WebSocket消息完毕，消息：{}", msg);
    }

    public static synchronized int getConnectionCount() {
        return count;
    }

    public static synchronized void addCount() {
        WebSocketServer.count++;
    }

    public static synchronized void subCount() {
        WebSocketServer.count--;
    }

}
