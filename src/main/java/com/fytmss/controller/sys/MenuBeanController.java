package com.fytmss.controller.sys;

import com.fytmss.beans.base.MenuBean;
import com.fytmss.common.utils.R;
import com.fytmss.service.base.MenuBeanService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wgq
 * @create 2024/7/26-周五 21:13
 */
@RestController
@RequestMapping("/sys/menu")
public class MenuBeanController {

    @Resource
    private MenuBeanService menuBeanService;

    @GetMapping("/menu")
    public R menu(String path){
        MenuBean menuBean = menuBeanService.selectMenuByPath(path);
        return null;
    }
}
