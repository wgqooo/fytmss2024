package com.fytmss.beans.base;

import lombok.Data;


import java.util.Date;

/**
 * @author wgq
 * @create 2024/8/20-周二 0:28
 */
@Data
public class OnlineUser {
    // session id
    private String sessionId;
    // 用户id
    private String userId;
    // 用户名称
    private String username;
    // 用户主机地址
    private String host;
    //用户ip归属地
    private String region;
    // 用户登录时系统IP
    private String systemHost;
    // 状态
    private String status;
    // session创建时间
    private String startTimestamp;
    // session最后访问时间
    private String lastAccessTime;
    // 超时时间
    private Long timeout;


}