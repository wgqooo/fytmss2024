package com.fytmss.service.base;

import com.fytmss.beans.base.OnlineUser;
import com.fytmss.beans.base.UserBean;
import com.fytmss.common.utils.DateUtils;
import com.fytmss.common.utils.IpUtils;
import jakarta.annotation.Resource;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author wgq
 * @create 2024/8/20-周二 0:33
 */
@Service
public class OnlineUserService {

    @Resource
    private SessionDAO sessionDAO;

    public List<OnlineUser> getAllOnlineUserList() {
        List<OnlineUser> onlineUserList = new ArrayList<>();
        //获取到当前所有有效的Session对象
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();
        OnlineUser userOnline;
        //循环遍历所有有效的Session
        for (Session session : activeSessions) {
            userOnline = new OnlineUser();
            UserBean user;
            SimplePrincipalCollection principalCollection;
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                user = (UserBean) principalCollection.getPrimaryPrincipal();
                userOnline.setUsername(user.getEmpName());
                userOnline.setUserId(user.getEmpNo());
            }
            userOnline.setSessionId((String) session.getId());
            userOnline.setHost(session.getHost());
            userOnline.setRegion(IpUtils.getIpRegion(session.getHost()));
            userOnline.setStartTimestamp(DateUtils.format(session.getStartTimestamp(), DateUtils.DATE_TIME_PATTERN));
            userOnline.setLastAccessTime(DateUtils.format(session.getLastAccessTime(), DateUtils.DATE_TIME_PATTERN));
            Long timeout = session.getTimeout();
            userOnline.setStatus(timeout.equals(0L) ? "离线" : "在线");
            userOnline.setTimeout(timeout);
            onlineUserList.add(userOnline);
        }
        return onlineUserList;
    }


    /**
     * 强制下线，这里可以暂时用不到
     * @param sessionId
     * @return
     */
    public boolean forceLogout(String sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        //强制注销
        sessionDAO.delete(session);
        return true;
    }
}
