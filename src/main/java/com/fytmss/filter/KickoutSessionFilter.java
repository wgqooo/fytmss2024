package com.fytmss.filter;

/**
 * @author wgq
 * @create 2024/5/19-周日 18:05
 */

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * 登录帐号控制过滤器
 *    其中的bug解决方式参考博客https://blog.51cto.com/u_16213599/10192423
 */
public class KickoutSessionFilter extends AccessControlFilter {

    private static final Logger logger = LoggerFactory.getLogger(KickoutSessionFilter.class);
    /**
     * 同一个用户最大会话数
     **/
    private int maxSession = 1;

    /**
     * 踢出之前登录的/之后登录的用户 默认false踢出之前登录的用户
     **/
    private Boolean kickoutAfter = false;
    /**
     * 踢出后到的地址
     **/
    private String kickoutUrl;
    /**
     * session管理器
     */
    private SessionManager sessionManager;
    /**
     * 缓存管理器
     */
    private Cache<String, Deque<Serializable>> cache;


    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(EhCacheManager ecacheManager) {
        this.cache = ecacheManager.getCache("kickoutSession");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    /**
     * 矛盾点：我需要subject判断当前用户是否认证登录，但是没有登录，shiro不让我调用SecurityUtils.getSubject()，否则报错UnavailableSecurityManagerException
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
//        Subject subject = null;
//        try{
//            subject = getSubject(request, response);
//        }catch (UnavailableSecurityManagerException e){
//            return true;
//        }
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return true;// 如果没有登录，直接进行之后的流程
        }
        Session session = subject.getSession();
        // 传的是userName 这里拿到的就是 userName
        // new SimpleAuthenticationInfo(userName, password, getName());
        String username = subject.getPrincipal().toString();
        Serializable sessionId = session.getId();

        //logger.info("进入KickoutControl, sessionId:{}", sessionId);
        // 同步控制
        Deque<Serializable> deque = cache.get(username);
        if (deque == null) {
            deque = new LinkedList<Serializable>();
            cache.put(username, deque);
        }

        // 如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            // 将用户的sessionId队列缓存
            deque.push(sessionId);
            cache.put(username, deque);
        }

        // 如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            // 如果踢出后者
            if (kickoutAfter) {
                kickoutSessionId = deque.removeFirst();
            }
            // 否则踢出前者
            else {
                kickoutSessionId = deque.removeLast();
            }
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if (kickoutSession != null) {
                    // 设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        // 如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null && (Boolean) session.getAttribute("kickout")) {
            // 会话被踢出了
            try {
                subject.logout();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            logger.info("用户登录人数超过限制, 重定向到{}", kickoutUrl);
//            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
//            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
//            WebUtils.issueRedirect(request, response ,kickoutUrl);
            return false;
        }
        return true;
    }



}