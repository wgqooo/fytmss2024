package com.fytmss.realm;

/**
 * @author wgq
 * @create 2024/5/17-周五 14:00
 */

import com.fytmss.beans.base.UserBean;
import com.fytmss.mapper.base.UserBeanMapper;
import jakarta.annotation.Resource;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 *     Realm 充当了 Shiro 与应用安全数据间的“桥梁”或者“连接器”,当对用户执行认证（登录）和授权（访问控制）验证时，
 * Shiro 会从应用配置的 Realm 中查找用户及其权限信息。从这个意义上讲，Realm 实质上是一个安全相关的 DAO：
 * 它封装了数据源的连接细节，并在需要时将相关数据提供给 Shiro 。当配置 Shiro时，你必须至少指定一个 Realm ，
 * 用于认证和（或）授权。配置多个 Realm 是可以的，但是至少需要一个。
 *     shiro内置了可以连接大量安全数据源的Realm，这里自定义数据源FyRealm
 * 1.创建一个类继承AuthorizingRealm类（实现了Realm接口类）
 * 2.重写doGetAuthorizationInfo和doGetAuthenticationInfo方法
 * 3.重写getName方法返回当前realm的自定义名称
 */
public class FyRealm extends AuthorizingRealm {

    @Resource
    private UserBeanMapper userMapper;

    /*
    权限获取：获取指定身份的权限，并返回相关信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /*
    身份验证：验证账户和密码，并返回相关信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //authenticationToken就是传递的subject.login(token)
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        UserBean user = userMapper.selectByPrimaryKey(username);
        //public SimpleAuthenticationInfo(Object principal, Object hashedCredentials, ByteSource credentialsSalt, String realmName)
        //这里传的principal是user
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                user,
                user.getEmpPassword(),
                ByteSource.Util.bytes(user.getEmpSalt()),
                getName()
        );
        return info;
    }
}
