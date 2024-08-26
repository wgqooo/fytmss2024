package com.fytmss.filter;

/**
 * @author wgq
 * @create 2024/5/19-周日 18:05
 * ehcache缓存中session的有效时间和服务器端session有效时间必须配置一致
 */

import com.fytmss.beans.base.UserBean;
import com.fytmss.common.utils.ServletUtils;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

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

    /**
     * 是否允许访问，返回 true 表示允许
     * 如果isAccessAllowed方法返回True，则不会再调用onAccessDenied方法，
     * 如果isAccessAllowed方法返回False,则会继续调用onAccessDenied方法。
     * 而onAccessDenied方法里面则是具体执行登陆的地方
     */
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
     * 返回 true: 表示已经处理了访问拒绝的情况，框架将不再进行其他权限检查，通常会终止请求或跳转到错误页面。
     * 返回 false: 表示未处理访问拒绝的情况，框架可能会继续执行其他过滤器或访问控制逻辑，可能会让请求继续进行或者重新进行权限验证。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return true;// 如果没有登录，直接进行之后的流程
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //设置参数false。获取当前用户的会话，如果当前用户没有会话（即没有活跃的会话），此方法将返回 null，而不会创建一个新的会话。
        //不设置参数false，如果当前用户没有会话（即没有活跃的会话），此方法会自动创建一个新的会话并返回这个新创建的会话对象。
        Session session = subject.getSession();
        // 传的是userName 这里拿到的就是 userName
        // new SimpleAuthenticationInfo(userName, password, getName());
        UserBean userBean = (UserBean) subject.getPrincipal();
        Serializable sessionId = session.getId();
        //logger.info("进入KickoutControl, sessionId:{}", sessionId);
        // 同步控制
        Deque<Serializable> deque = cache.get(userBean.getEmpNo());
        if (deque == null) {
            deque = new LinkedList<Serializable>();
            cache.put(userBean.getEmpNo(), deque);
        }

        // 如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            // 将用户的sessionId队列缓存，push往队头加
            deque.push(sessionId);
            cache.put(userBean.getEmpNo(), deque);
        }

        // 如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            // 如果踢出后者
            if (kickoutAfter) {
                kickoutSessionId = deque.removeFirst();
            } else {// 否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            cache.put(userBean.getEmpNo(), deque);
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if (kickoutSession != null) {
                    // 设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {
                logger.info(e.getMessage() + ", 已被销毁");
            }
        }

        // 如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null && (Boolean) session.getAttribute("kickout")) {
            // 会话被踢出了
            try {
                //退出登录 与验证相对的是释放所有已知的身份信息，当Subject 与程序不再交互了，
                // 你可以调用subject.logout() 丢掉所有身份信息。 当你调用logout，
                // 任何现存的session 将变为不可用并且所有的身份信息将消失
                subject.logout();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            if (ServletUtils.isAjaxRequest(httpServletRequest)) {
                //销毁会话之后首先解决跨域问题，然后传数据给axios，重定向到登陆页面
                httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
                httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("origin"));
                httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
                httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
                httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
                httpServletResponse.addHeader("session-status", "two-user");
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "session-status");
                //httpServletResponse.setStatus(401);
            }
            return false;
        }
        return true;
    }



}