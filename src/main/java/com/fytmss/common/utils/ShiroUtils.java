package com.fytmss.common.utils;

import com.fytmss.beans.base.UserBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * @author wgq
 * @create 2024/5/19-周日 22:39
 */
public class ShiroUtils {

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    /**获取shiro的连接器*/
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**获取登录用户的信息*/
    public static UserBean getUser() {
        return (UserBean) SecurityUtils.getSubject().getPrincipal();
    }

    /**获取登录用户的id*/
    public static String getUserId() {
        return getUser().getEmpNo();
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    /**用于判断有没有获取登录用户的信息*/
    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null;
    }
    /**注销用户*/
    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    public static String getKaptcha(String key) {
        String kaptcha = getSessionAttribute(key).toString();
        getSession().removeAttribute(key);
        return kaptcha;
    }

}