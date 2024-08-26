package com.fytmss.realm;

/**
 * @author wgq
 * @create 2024/5/17-周五 14:00
 */

import com.fytmss.beans.base.RoleBean;
import com.fytmss.beans.base.UserBean;
import com.fytmss.mapper.base.UserBeanMapper;
import jakarta.annotation.Resource;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.ArrayList;
import java.util.List;

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
    权限配置在数据库中，@RequiresPermissions的增强最终会调用到Realm接口，
    这个接口里定义了两个方法，一个是用户信息查询，一个是权限查询。通常我们自己定义实现类来实现Realm接口的这两个方法，
    去查询数据库，获得用户信息和权限信息。@RequiresPermissions的增强最终就是调用了查询权限信息的方法，
    通过数据库查询出来的权限，与@RequiresPermissions参数中的权限比对，如果有相同的权限则放行，如果没有则不能访问

    Shiro 在进行权限判断时，通常会使用缓存来提高性能。默认情况下，Shiro 会对权限信息进行缓存，这包括 doGetAuthorizationInfo() 方法获取的授权信息。
    这意味着，如果某个用户的权限信息已经被查询并缓存，那么在一定时间内，不会反复调用 doGetAuthorizationInfo() 方法。
    ●当一个方法被 @RequiresPermissions("sys:menu:list") 注解标注时，Shiro 首先会检查当前用户的权限是否已经被缓存。
    ●如果权限信息已经缓存并且包含了 "sys:menu:list" 权限，则直接允许方法执行，而无需再次调用 doGetAuthorizationInfo() 方法。
    ●如果权限信息没有被缓存，或者缓存过期，Shiro 将会调用 doGetAuthorizationInfo() 方法获取最新的用户授权信息，然后进行权限验证。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("***********" + principals.getPrimaryPrincipal());
        // 获取登录用户名
        //String name = (String)principals.getPrimaryPrincipal();
        // 查询用户名称
//        UserBean user = userMapper.selectByPrimaryKey(name);
//        // 添加角色和权限
//        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        List<String> roleNameList = new ArrayList<>();
//        List<String> permissionNameList = new ArrayList<>();
//
//        for (RoleBean role : user.getRoles()) {
//            roleNameList.add(role.getRoleName());
//            for (Permission permission : role.getPermissions()) {
//                permissionNameList.add(role.getRoleName()+":"+permission.getPermissionName());
//            }
//        }
//        // 添加角色
//        simpleAuthorizationInfo.addRoles(roleNameList);
//        // 添加权限
//        simpleAuthorizationInfo.addStringPermissions(permissionNameList);
//        return simpleAuthorizationInfo;
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


    /**
     * 重写方法,清除当前用户的的 授权缓存
     * @param principal
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principal) {
        super.clearCachedAuthorizationInfo(principal);
    }
    /**
     * 重写方法，清除当前用户的 认证缓存
     * @param principal
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principal) {
        super.clearCachedAuthenticationInfo(principal);
    }

    /**
     *  重写方法，清除当前用户的 认证缓存和授权缓存
     * */
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有用户的 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有用户的 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有用户的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}
