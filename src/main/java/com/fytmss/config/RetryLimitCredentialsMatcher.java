package com.fytmss.config;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * @author wgq
 * @create 2024/5/19-周日 15:04
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {
    private static final Logger log = LoggerFactory.getLogger(RetryLimitCredentialsMatcher.class);

    //集群中可能会导致出现验证多过5次的现象，因为AtomicInteger只能保证单节点并发
    private final Cache<String, AtomicInteger> loginRetryCache;

    private int maxRetryCount = 5;

    private String loginRetryCacheName;

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public RetryLimitCredentialsMatcher(CacheManager cacheManager,String loginRetryCacheName) {
        this.loginRetryCacheName = loginRetryCacheName;
        loginRetryCache = cacheManager.getCache(loginRetryCacheName);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = loginRetryCache.get(username);
        if (null == retryCount) {
            retryCount = new AtomicInteger(0);
            loginRetryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() > 5) {
            log.warn("username: " + username + " tried to login more than 5 times in period");
            throw new ExcessiveAttemptsException("用户: '" + username + "'登陆失败次数过多，请10分钟后重试");
        }
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //clear retry data
            loginRetryCache.remove(username);
        }
        return matches;
    }
}
