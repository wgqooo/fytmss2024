package com.fytmss.controller.sys;

import com.fytmss.beans.form.LoginForm;
import com.fytmss.beans.base.UserBean;
import com.fytmss.common.utils.R;
import com.fytmss.common.utils.ShiroUtils;
import com.fytmss.service.base.OnlineUserService;
import com.fytmss.service.base.UserBeanService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wgq
 * @create 2024/5/18-周六 14:41
 */
@RestController
@RequestMapping("/sys")
public class LoginController {

    @Resource
    private UserBeanService userService;


    //当用户在浏览器的地址栏中直接输入某个URL地址或者单击网页上的某个超链接时,浏览器会使用GET方法向服务器发送请求
    //离谱！！！ /sys必须写在postMapping里面，不然报错404，笑了，卡我这么久！！！！！！！！！
    //他喵的，全局路径配置写在RequestMapping里面，而不是RestController里面
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginForm loginForm){
        UserBean user = userService.selectByPrimaryKey(loginForm.getUsername());
        if(user == null){
            return R.error("账号或者密码有误");
        }
        try{
            userService.checkLogin(loginForm.getUsername(), loginForm.getPassword(), false);
        } catch (ExcessiveAttemptsException e){
            return R.error(e.getMessage());
        }catch (AuthenticationException e){
            return R.error("账号或者密码有误");
        }
        if(user.getEnabled() == 0) {
            return R.error("该账号已被锁定，请联系相关管理员");
        }
        // todo 登陆成功用户账号存入session
        UserBean principal = ShiroUtils.getUser();
        Serializable sessionId = SecurityUtils.getSubject().getSession().getId();
        R r = new R();
        //获取当前登录的用户
        r.put("name", principal.getEmpName());
        r.put("sessionId", sessionId);
        r.put("msg", "登陆成功");
        return r;
    }

    @PostMapping("/logout")
    public R logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return R.ok("退出登录成功");
    }
}
