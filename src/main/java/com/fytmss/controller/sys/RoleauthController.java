package com.fytmss.controller.sys;

import com.fytmss.beans.base.MenuBean;
import com.fytmss.beans.base.RoleauthBean;
import com.fytmss.common.utils.R;
import com.fytmss.service.base.MenuBeanService;
import com.fytmss.service.base.RoleBeanService;
import com.fytmss.service.base.RoleauthBeanService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wgq
 * @create 2024/7/19-周五 0:28
 */
@RestController
@RequestMapping("/sys/auth")
public class RoleauthController {

//    @Resource
//    private RoleBeanService roleBeanService;

    @Resource
    private RoleauthBeanService roleauthBeanService;

    @Resource
    private MenuBeanService menuBeanService;

    @GetMapping("/roles")
    public R roles(Integer menuId){
        List<String> roles = null;
        R r = new R();
        roles = roleauthBeanService.getRoleCNameByMenuId(menuId);
        r.put("roles", roles);
        return r;
    }

    /**
     * @param arr：数组的最后一个元素是要赋予权限的角色roleId，其他元素是要赋予的权限的menuId
     * @return
     */
    @PostMapping("/addAuth")
    public R addAuth(@RequestBody int[] arr){
        if(arr.length > 0){
            int roleId = arr[arr.length - 1];
            //roleId对应的角色原有的权限
            List<Integer> oldAuth = roleauthBeanService.getRoleauthByRoleId(roleId);
            for(int i = 0; i < arr.length - 1; i++){
                //每个arr[i]都表示一个menuId
                if(oldAuth.contains(arr[i])){
                    //相同部分权限不变，将oldAuth多余的权限保留，方便后面删除
                    //记住：使用list.remove(o)，而不是list.remove(i)
                    oldAuth.remove((Integer) arr[i]);
                }else{
                    //arr数组的不同权限需要添加,roleMenuId数组库自动递增
                    roleauthBeanService.insert(new RoleauthBean(null, roleId, arr[i]));
                }
            }
            //oldAuth多余的权限需要删除
            for (int i : oldAuth) {
                roleauthBeanService.deleteAuth(roleId, i);
            }
        }
        return R.ok();
    }
}
