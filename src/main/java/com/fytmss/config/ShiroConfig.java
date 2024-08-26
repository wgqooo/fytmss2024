package com.fytmss.config;

import com.fytmss.filter.ClearSessionCacheFilter;
import com.fytmss.filter.KickoutSessionFilter;
import com.fytmss.realm.FyRealm;
import jakarta.servlet.Filter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Base64;

/**
 * @author wgq
 * @create 2024/5/17-周五 10:38
 */
@Configuration
public class ShiroConfig {

    // todo 存疑？？？？？？？？？？？？
    @Bean
    public MethodInvokingFactoryBean getMethodInvokingFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(new Object[]{defaultWebSecurityManager});
        return factoryBean;
    }


    @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return ehCacheManager;
    }

    @Bean
    public ShiroSessionListener shiroSessionListener(){
        return new ShiroSessionListener();
    }

    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setCacheManager(ehCacheManager());
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        sessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return sessionDAO;
    }

    @Bean
    public SimpleCookie sessionIdCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

//    @Bean
//    public ClearSessionCacheFilter clearSessionCacheFilter() {
//        ClearSessionCacheFilter clearSessionCacheFilter = new ClearSessionCacheFilter();
//        return clearSessionCacheFilter;
//    }

    @Bean
    public DefaultWebSessionManager defaultWebSessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        listeners.add(shiroSessionListener());
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(ehCacheManager());
        sessionManager.setGlobalSessionTimeout(1000 * 60 * 30);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationInterval(1000 * 60 * 60);
        //TODO 后续是否需要配置sessionDAO
        return sessionManager;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(defaultWebSecurityManager());
        return advisor;
    }

    @Bean
    public RetryLimitCredentialsMatcher getRetryLimitCredentialsMatcher(){
        RetryLimitCredentialsMatcher matcher = new RetryLimitCredentialsMatcher(ehCacheManager(), "loginRetryCache");
        matcher.setHashAlgorithmName("SHA-256");
        matcher.setStoredCredentialsHexEncoded(true);
        matcher.setHashIterations(3);
        return matcher;
    }

    @Bean
    public FyRealm fyRealm(){
        FyRealm fyRealm = new FyRealm();
        fyRealm.setCredentialsMatcher(getRetryLimitCredentialsMatcher());
        fyRealm.setCachingEnabled(true);
        fyRealm.setAuthenticationCachingEnabled(true);
        fyRealm.setAuthenticationCacheName("authenticationCache");
        fyRealm.setAuthorizationCachingEnabled(true);
        fyRealm.setAuthorizationCacheName("authorizationCache");
        return fyRealm;
    }


    @Bean
    public SimpleCookie rememberMeCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.getDecoder().decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }


    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(fyRealm());
        securityManager.setCacheManager(ehCacheManager());
        securityManager.setSessionManager(defaultWebSessionManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(defaultWebSecurityManager());
        LinkedHashMap<String, Filter> filtersMap = new LinkedHashMap<>();
        //  todo 限制同一帐号同时在线的个数
        filtersMap.put("kickout", kickoutSessionControlFilter());
        // todo 拦截session过期的操作
        shiroFilter.setFilters(filtersMap);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/sys/**", "anon");
        map.put("/webSocket/**", "anon");
        map.put("/base/**", "anon");
        map.put("/voyage/**", "anon");
        map.put("/ticket/**", "anon");
        map.put("/query/**", "anon");
        map.put("/finance/**", "anon");
        map.put("/static/**", "authc");
        map.put("/**", "kickout,authc");
        shiroFilter.setFilterChainDefinitionMap(map);
        return shiroFilter;
    }

    @Bean
    public KickoutSessionFilter kickoutSessionControlFilter() {
        KickoutSessionFilter kickoutSessionControlFilter = new KickoutSessionFilter();
        kickoutSessionControlFilter.setSessionManager(defaultWebSessionManager());
        kickoutSessionControlFilter.setCacheManager(ehCacheManager());
        kickoutSessionControlFilter.setKickoutAfter(false);
        kickoutSessionControlFilter.setMaxSession(1);
        kickoutSessionControlFilter.setKickoutUrl("/");
        return kickoutSessionControlFilter;
    }
}
