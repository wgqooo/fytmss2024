package com.fytmss.config;

import lombok.Getter;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wgq
 * @create 2024/8/13-周二 20:35
 */

public class ShiroSessionListener implements SessionListener {

    /**
     * 统计在线人数
     * juc包下线程安全自增
     */
    @Getter
    private final static AtomicInteger sessionCount = new AtomicInteger(0);

    /**
     * 会话创建时触发
     * @param session
     */
    @Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
        System.err.println("会话号【" + session.getId() + "】创建");
    }

    /**
     * 退出会话时触发
     * @param session
     */
    @Override
    public void onStop(Session session) {
        //会话退出,在线人数减一
        sessionCount.decrementAndGet();
        System.err.println("会话号【" + session.getId() + "】退出");
    }
    /**
     * 会话过期时触发
     * @param session
     */
    @Override
    public void onExpiration(Session session) {
        sessionCount.decrementAndGet();
//        try {
//            //这里是发送给自己会话的前端，告知该用户重新登陆
//            WebSocketServer.sendInfo("timeout", session.getId().toString());
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        System.err.println("会话号【" + session.getId() + "】过期");
    }

}
