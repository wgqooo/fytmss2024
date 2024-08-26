package com.fytmss.controller.base;

import com.fytmss.beans.base.RoleBean;
import com.fytmss.common.utils.R;
import com.fytmss.service.base.RoleBeanService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wgq
 * @create 2024/6/4-周二 19:50
 */
@RestController
@RequestMapping("/base/role")
public class RoleController {

    @Resource
    private RoleBeanService roleBeanService;

    @GetMapping("/list")
    public R list(){
        List<RoleBean> roles = roleBeanService.getAllRoles();
        R r = new R();
        r.put("roles", roles);
        return r;
    }

    @PostMapping("/save")
    public R save(@RequestBody RoleBean roleBean){
        int total = roleBeanService.getTotal();
        try{
            roleBean.setRoleId(total + 1);
            roleBeanService.insert(roleBean);
        }catch (Exception e){
            return R.error("该角色已经存在");
        }
        return R.ok("添加成功");
    }

    @PostMapping("/update")
    public R update(@RequestBody RoleBean roleBean){
        try{
            int i = roleBeanService.updateRole(roleBean);
            if(i == 0) return R.error("角色修改失败");
        }catch (Exception e){
            return R.error("修改失败，请联系管理员");
        }
        return R.ok("修改成功");
    }

    @DeleteMapping("/delete")
    public R delete(@RequestParam Integer roleId){
        try{
            roleBeanService.deleteByPrimaryKey(roleId);
        }catch (Exception e){
            return R.error("删除失败，请联系管理员");
        }
        return R.ok("删除成功");
    }
}
