package com.fytmss.common.utils;

import com.fytmss.config.WebSocketServer;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author wgq
 * @create 2024/8/12-周一 14:26
 */
@Slf4j
public class WebSocketManager {

    private final static CopyOnWriteArraySet<WebSocketServer> webSocketServerSet = new CopyOnWriteArraySet<>();

    private final static ConcurrentHashMap<String, WebSocketServer> webSocketServerMap = new ConcurrentHashMap<>();

    public static int size(){
        return webSocketServerSet.size();
    }

    public static void addWebSocketServer(WebSocketServer webSocketServer){
        if (webSocketServer != null){
            webSocketServerSet.add(webSocketServer);
            webSocketServerMap.put(webSocketServer.getSessionId(), webSocketServer);
        }
    }

    public static void removeWebSocketServer(WebSocketServer webSocketServer){
        webSocketServerSet.remove(webSocketServer);
        webSocketServerMap.remove(webSocketServer.getSessionId());
    }

    /**
     * 通过SessionId发送消息给特定用户
     * @param
     * @param msg
     */
    public static void sentToUser(String sessionId, String msg){
        Session session = webSocketServerMap.get(sessionId).getSession();
        sentToUser(session, msg);
    }

    /**
     * 通过Session发送消息给特定用户
     * @param session
     * @param msg
     */
    public static void sentToUser(Session session, String msg){
        if (session == null){
            log.error("不存在该Session，无法发送消息");
            return;
        }
        session.getAsyncRemote().sendText(msg);
    }

    /**
     * 发送消息给所有用户
     * @param msg
     */
    public static void sentToAllUser(String msg){
        for (WebSocketServer webSocketServer : webSocketServerSet) {
            sentToUser(webSocketServer.getSession(), msg);
        }
        log.info("向所有用户发送WebSocket消息完毕，消息：{}", msg);
    }

    //public static void sendOnli

    /**
     * 更新在线人数
     */
    public static void updateOnlineCount(){
        for (WebSocketServer webSocketServer : webSocketServerSet) {
            sentToUser(webSocketServer.getSession(), String.valueOf(size()));
        }
    }
}
