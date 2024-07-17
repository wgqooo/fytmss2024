package com.fytmss.controller.base;

import com.fytmss.beans.base.ShipBean;
import com.fytmss.common.utils.R;
import com.fytmss.service.base.ShipBeanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author wgq
 * @create 2024/5/31-周五 10:46
 */
@RestController
@RequestMapping("/base/ship")
public class ShipController {

    @Resource
    private ShipBeanService shipBeanService;

    @GetMapping("/list")
    public R list(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        PageInfo<ShipBean> ships = shipBeanService.findAllShipByPageS(index, size);
        R r = new R();
        r.put("page", ships);
        return r;
    }

    @PostMapping("/save")
    public R save(@RequestBody ShipBean shipBean){
        try{
            shipBeanService.insert(shipBean);
        }catch (Exception e){
            return R.error("该船号已经存在");
        }
        return R.ok("添加成功");
    }

    @PostMapping("/update")
    public R update(@RequestBody ShipBean shipBean){
        try{
            int i = shipBeanService.updateByPrimaryKey(shipBean);
            if(i == 0) return R.error("不能修改船舷号");
        }catch (Exception e){
            return R.error("修改失败，请联系管理员");
        }
        return R.ok("修改成功");
    }

    @DeleteMapping("/delete")
    public R delete(@RequestParam String shipNo){
        try{
            shipBeanService.deleteByPrimaryKey(shipNo);
        }catch (Exception e){
            return R.error("删除失败，请联系管理员");
        }
        return R.ok("删除成功");
    }

    @GetMapping("/listAllShips")
    public R listAllShips(){
        List<ShipBean> ships = shipBeanService.findAllShips();
        R r = new R();
        r.put("ships", ships);
        return r;
    }
}
