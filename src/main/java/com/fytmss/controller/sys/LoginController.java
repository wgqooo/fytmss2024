package com.fytmss.controller.sys;

import com.fytmss.beans.form.LoginForm;
import com.fytmss.beans.base.UserBean;
import com.fytmss.common.utils.R;
import com.fytmss.common.utils.ShiroUtils;
import com.fytmss.service.base.UserBeanService;
import jakarta.annotation.Resource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        R r = new R();
        //获取当前登录的用户
        UserBean principal = ShiroUtils.getUser();
        r.put("name", principal.getEmpName());
        r.put("msg", "登陆成功");
        return r;
    }
}
