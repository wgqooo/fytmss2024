package com.fytmss.controller.base;

import com.fytmss.beans.base.OnlineUser;
import com.fytmss.beans.base.RoleBean;
import com.fytmss.beans.base.UserBean;
import com.fytmss.beans.base.UserRoleBean;
import com.fytmss.beans.form.LoginForm;
import com.fytmss.common.constant.RoleNameMap;
import com.fytmss.common.utils.R;
import com.fytmss.service.base.OnlineUserService;
import com.fytmss.service.base.RoleBeanService;
import com.fytmss.service.base.UserBeanService;
import com.fytmss.service.base.UserRoleBeanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.http.HttpStatus;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * @author wgq
 * @create 2024/5/17-周五 16:17
 */
@RestController
@RequestMapping("/base/user")
public class UserController {

    @Resource
    private UserBeanService userService;

    @Resource
    private UserRoleBeanService userRoleBeanService;

    @Resource
    private RoleBeanService roleBeanService;

    @Resource
    private OnlineUserService onlineUserService;

    @GetMapping("online")
    public R online(){
        List<OnlineUser> onlineUserList = onlineUserService.getAllOnlineUserList();
        R r = new R();
        r.put("onlineUserList", onlineUserList);
        return r;
    }


    /*
    页面上需要对返回的数据进行展示就需要用到分页效果了。
而前端要做的就是尽快接受数据并最快地展示给用户，对于数据不多的场景用前端实现也无妨，
然而若考虑到以后会有成千上万条数据应用的场景，显然后端去处理分页更合适些。
每次点击下一页，前端只需发送分页数信息请求后端数据，假设一页显示十条数据，
每次点击只需请求这十条数据的信息返回给前端来更快地进行交互。然而若是由前端来进行分页操作，
那就得把成千上万条数据全部先拉下来，再进行操作，先不说操作这么多数据拉低的性能，
光是先拉下来就得费很长时间了，所以对于数据量大的操作，一般都采用后端分页的操作更合适。
首先要了解为什么要分页。分页主要是为了避免一次性从数据库获取大量数据。其次才是为了展示效果。
     pagehelper本质就是在我们执行SQL语句之前，自动给SQL语句拼接了分页的语句
     */
    @GetMapping("/list")
    public R list(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        PageInfo<UserBean> users = userService.findAllUserByPageS(index, size);
        R r = new R();
        r.put("page", users);
        return r;
    }

    @PostMapping("/update")
    public R update(@RequestBody UserBean userBean){
        System.out.println(userBean.getEmpBirthday());
        try{
            userService.updateByPrimaryKey(userBean);
            UserRoleBean userRole = userRoleBeanService.getUserRoleBean(userBean.getEmpNo());
            userRole.setRoleId(RoleNameMap.map.get(userBean.getRoleName()));
            userRoleBeanService.updateByPrimaryKey(userRole);
        } catch (Exception e){
            return R.error("员工编号不能修改");
        }
        return R.ok("保存成功");
    }

    @PostMapping("/save")
    public R save(@RequestBody UserBean userBean){
        String salt = UUID.randomUUID().toString().replace("-","");
        Sha256Hash hash = new Sha256Hash(userBean.getEmpBirthday().replace("-",""),
                salt, 3);
        userBean.setEmpPassword(hash.toHex());
        userBean.setEmpSalt(salt);
        //userBean.setEnabled(1);
        try{
            userService.insert(userBean);
        }catch (Exception e){
            return R.error(HttpStatus.SC_BAD_REQUEST,"该员工编号已经存在");
        }
        userRoleBeanService.insert(new UserRoleBean(null, userBean.getEmpNo(), RoleNameMap.map.get(userBean.getRoleName())));
        return R.ok("添加成功");
    }

    @DeleteMapping("/delete")
    public R delete(@RequestParam String empNo){
        try{
            //先删除对应的权限
            userRoleBeanService.deleteByEmpNo(empNo);
            userService.deleteByPrimaryKey(empNo);
        }catch (Exception e){
            return R.error("删除失败，请联系管理员");
        }
        return R.ok("删除成功");
    }

    @PutMapping("/enable")
    public R enable(@RequestParam Integer isEnabled, String empNo){
        //System.out.println("isEnabled=" + isEnabled + ", empNo=" + empNo);
        try{
            userService.isFreezeEmp(isEnabled, empNo);
        }catch (Exception e){
            return R.error("冻结/解冻失败，请联系管理员");
        }
        if(isEnabled == 1){
            return R.ok("解冻成功");
        }else{
            return R.ok("冻结成功");
        }
    }


    //todo
    @PutMapping("/password")
    public R password(@RequestBody LoginForm loginForm){
        UserBean userBean = userService.selectByPrimaryKey(loginForm.getUsername());
        String salt = userBean.getEmpSalt();
        Sha256Hash hash = new Sha256Hash(loginForm.getPassword(),
                salt, 3);
        //userBean.setEmpPassword(hash.toHex());
        try{
            userService.resetPassword(loginForm.getUsername(), hash.toHex());
        }catch (Exception e){
            return R.error("修改失败，请联系管理员");
        }
        return R.ok();
    }

    @GetMapping("/role")
    public R role(String empNo){
        UserRoleBean userRoleBean = userRoleBeanService.getUserRoleBean(empNo);
        RoleBean roleBean = roleBeanService.selectRoleByRoleId(userRoleBean.getRoleId());
        R r = new R();
        r.put("role", roleBean.getRoleCname());
        return r;
    }

}
