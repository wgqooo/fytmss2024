package com.fytmss.config;

import com.fytmss.filter.KickoutSessionFilter;
import com.fytmss.realm.FyRealm;
import jakarta.servlet.Filter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wgq
 * @create 2024/5/17-周五 10:38
 */
@Configuration
public class ShiroConfig {


    /**
     * shiro 是支持缓存功能的，它可以对用户的授权数据和认证数据进行缓存，使得用户不必每次认证或授权时都去查询数据库
     * @return ehCacheManager需要添加到securityManager中
     */
    @Bean
    public EhCacheManager getEhCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return ehCacheManager;
    }

    /**
     * session会话验证调度器
     * @return session会话验证调度器
     */
//    @Bean
//    public ExecutorServiceSessionValidationScheduler getConfigSessionValidationScheduler() {
//        ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
//        //设置session的失效扫描间隔，单位为毫秒
//        sessionValidationScheduler.setInterval(10*60*1000);
//        return sessionValidationScheduler;
//    }

    /**
     * Session Manager：会话管理
     * 即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中；
     * 会话可以是普通JavaSE环境的，也可以是如Web环境的；
     */
    @Bean
    public DefaultWebSessionManager getDefaultWebSessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //1 设置session过期时间,默认30分钟？
        //sessionManager.setGlobalSessionTimeout(150*1000);
        //2 定期验证session
        //sessionManager.setSessionValidationSchedulerEnabled(true);
        //sessionManager.setSessionValidationScheduler(scheduler);
        //3 删除无效session
        sessionManager.setDeleteInvalidSessions(true);
        //4 去掉shiro登录时url里面的JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        //TODO 后续是否需要配置sessionDAO
        return sessionManager;
    }


    /**
     *每当一个会话被创建或更新时，它的数据需要持久化到一个存储位置以便它能够被稍后的应用程序访问。
     *同样地， 当一个会话失效且不再被使用时，它需要从存储中删除以便会话数据存储空间不会被耗尽。
     *SessionManager 实现委 托这些 Create/Read/Update/Delete(CRUD)操作为内部组件，
     *同时，SessionDAO，反映了数据访问对象（DAO）设计 模式。
     *SessionDAO 的权力是你能够实现该接口来与你想要的任何数据存储进行通信。
     *这意味着你的会话数据可以驻留在内存中，文件系统，关系数据库或 NoSQL 的数据存储，或其他任何你需要的位置。你得控制持久性行为。
     */

    /**
     * 开启Shiro注解(如@RequiresRoles,@RequiresPermissions),
     * 切面Advisor是切点和增强的复合体，Advisor本身已经包含了足够的信息，如横切逻辑以及连接点
     * DefaultAdvisorAutoProxyCreator能偶扫描Advisor，并将Advisor自动织入匹配的目标Bean中，为匹配的目标Bean自动创建代理
     * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * @return creator
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    /**
     * 开启aop注解支持
     * @param securityManager
     * @return advisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public RetryLimitCredentialsMatcher getRetryLimitCredentialsMatcher(EhCacheManager ehCacheManager){
        RetryLimitCredentialsMatcher matcher = new RetryLimitCredentialsMatcher(ehCacheManager, "loginRetryCache");
        //matcher就是用来指定加密规则
        //加密方式
        matcher.setHashAlgorithmName("SHA-256");
        //是Hex编码的还是Base64编码的。对应SimpleHash的toHex()和toBase64()
        matcher.setStoredCredentialsHexEncoded(true);
        //hash次数（对密码进行加密几次？）
        matcher.setHashIterations(3);
        return matcher;
    }

    @Bean
    public FyRealm getFyRealm(HashedCredentialsMatcher matcher){
        FyRealm fyRealm = new FyRealm();
        fyRealm.setCredentialsMatcher(matcher);
        return fyRealm;
    }

    /**
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     * @return cookie管理对象;
     */
    @Bean
    public CookieRememberMeManager getCookieRememberMeManager(){
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(30*24*60*60);
        rememberMeManager.setCookie(simpleCookie);
        return rememberMeManager;
    }



    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(FyRealm fyRealm, EhCacheManager ehCacheManager,
                                                                  DefaultWebSessionManager sessionManager, CookieRememberMeManager rememberMeManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(fyRealm);
        // 将 EhCacheManager 注入到 SecurityManager 中，否则不会生效
        securityManager.setCacheManager(ehCacheManager);
        // 将 sessionManager 注入到 SecurityManager 中，否则不会生效
        securityManager.setSessionManager(sessionManager);
        // 将 CookieRememberMeManager 注入到 SecurityManager 中，否则不会生效
        securityManager.setRememberMeManager(rememberMeManager);
        //ThreadContext.bind(securityManager);//加上这句代码手动绑定
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager, KickoutSessionFilter kickoutSessionControlFilter){
        //System.out.println("ShiroFilterFactoryBean->DefaultWebSessionManager:" + securityManager);
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        // Shiro的核心安全接口,这个属性是必须的
        shiroFilter.setSecurityManager(securityManager);

        //不输入地址的话会自动寻找
        shiroFilter.setLoginUrl("/login");
        //登录成功默认跳转页面，不配置则跳转至”/”。如果登陆前点击的一个需要登录的页面，则在登录自动跳转到那个需要登录的页面。不跳转到此。
        shiroFilter.setSuccessUrl("/");

        // 自定义拦截器限制并发人数
        LinkedHashMap<String, Filter> filtersMap = new LinkedHashMap<>();
        // 限制同一帐号同时在线的个数
        filtersMap.put("kickout", kickoutSessionControlFilter);
        shiroFilter.setFilters(filtersMap);
        Map<String, String> map = new LinkedHashMap<>();
        // 不能对login方法进行拦截，若进行拦截的话，这辈子都登录不上去了,这个login是LoginController里面登录校验的方法
        map.put("/sys/login", "anon");
        map.put("/sys/register", "anon");
        //没做权限之前，要不先这样？HHHHH
        map.put("/base/**", "anon");
        map.put("/voyage/**", "anon");
        map.put("/ticket/**", "anon");
        map.put("/query/**", "anon");
        map.put("/finance/**", "anon");
        map.put("/static/**", "authc");
        //对所有用户认证
        //表示 访问 /** 下的资源首先要通过 kickout 对应的 filter 拦截处理下，然后再通过 authc 后面对应的 filter 才可以访问。
        map.put("/**", "kickout,authc");
        shiroFilter.setFilterChainDefinitionMap(map);
        return shiroFilter;
    }

    /**
     * 并发登录控制
     *  将kickoutSessionControlFilter的代码，移到ShiroFilterFactoryBean的bean代码后面就行？
     * @return
     */
    @Bean
    public KickoutSessionFilter getKickoutSessionControlFilter(DefaultWebSessionManager sessionManager, EhCacheManager ehCacheManager) {

        KickoutSessionFilter kickoutSessionControlFilter = new KickoutSessionFilter();
        // 用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionControlFilter.setSessionManager(sessionManager);
        // 使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        kickoutSessionControlFilter.setCacheManager(ehCacheManager);
        // 是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；
        kickoutSessionControlFilter.setKickoutAfter(false);
        // 同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionControlFilter.setMaxSession(1);
        // 被踢出后重定向到的地址；
        //kickoutSessionControlFilter.setKickoutUrl("www.baidu.com");
        return kickoutSessionControlFilter;
    }
}
