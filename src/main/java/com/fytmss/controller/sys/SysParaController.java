package com.fytmss.controller.sys;

import com.fytmss.beans.base.SysparaBean;
import com.fytmss.common.utils.R;
import com.fytmss.service.sys.SysparaBeanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author wgq
 * @create 2024/7/16-周二 14:44
 */
@RestController
@RequestMapping("/sys/syspara")
public class SysParaController {

    @Resource
    private SysparaBeanService sysparaBeanService;

    @GetMapping("/list")
    public R list(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        PageInfo<SysparaBean> sysparams = sysparaBeanService.findAllParamsByPage(index, size);
        R r = new R();
        r.put("page", sysparams);
        return r;
    }

    @GetMapping("/sort")
    public R sort(){
        List<String> types = sysparaBeanService.findAllTypes();
        R r = new R();
        r.put("types", types);
        return r;
    }

    @PostMapping("/save")
    public R save(@RequestBody SysparaBean sysparaBean){
        System.out.println(sysparaBean);
        try{
            sysparaBeanService.insert(sysparaBean);
        }catch (Exception e){
            return R.error("添加失败，请联系管理员");
        }
        return R.ok("添加成功");
    }

    @PostMapping("/update")
    public R update(@RequestBody SysparaBean sysparaBean){
        try{
            int i = sysparaBeanService.update(sysparaBean);
            if(i == 0) return R.error("修改失败，请联系管理员");
        }catch (Exception e){
            return R.error("修改失败，请联系管理员");
        }
        return R.ok("修改成功");
    }

    @DeleteMapping("/delete")
    public R delete(String dType, Integer dCode){
        try{
            sysparaBeanService.delete(dType, dCode);
        }catch (Exception e){
            return R.error("删除失败，请联系管理员");
        }
        return R.ok("删除成功");
    }
}
