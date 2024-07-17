package com.fytmss.controller.base;

import com.fytmss.beans.base.RoleBean;
import com.fytmss.common.utils.R;
import com.fytmss.service.base.RoleBeanService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
