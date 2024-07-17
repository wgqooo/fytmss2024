package com.fytmss.controller.base;

import com.fytmss.beans.base.TravelBean;
import com.fytmss.beans.base.UserBean;
import com.fytmss.beans.base.UserRoleBean;
import com.fytmss.common.utils.R;
import com.fytmss.service.base.TravelBeanService;
import com.fytmss.service.base.UserBeanService;
import com.fytmss.service.base.UserRoleBeanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * @author wgq
 * @create 2024/6/1-周六 14:59
 */
@RestController
@RequestMapping("/base/travel")
public class TravelController {

    @Resource
    private TravelBeanService travelBeanService;

    @Resource
    private UserBeanService userBeanService;

    @Resource
    private UserRoleBeanService userRoleBeanService;


    @GetMapping("/list")
    public R list(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        PageInfo<TravelBean> travels = travelBeanService.findAllTravelsByPageS(index, size);
        R r = new R();
        r.put("page", travels);
        return r;
    }

    @PostMapping("/save")
    public R save(@RequestBody TravelBean travelBean){
        travelBean.setIsDel(0);
        String pwd = travelBean.getLegalNo().length() - 6 >= 0 ? travelBean.getLegalNo().substring(travelBean.getLegalNo().length() - 6) : travelBean.getLegalNo();
        String salt = UUID.randomUUID().toString().replace("-","");
        Sha256Hash sha256Hash = new Sha256Hash(pwd, salt, 3);
        travelBean.setTravelPassword(sha256Hash.toHex());
        try{
            travelBeanService.insert(travelBean);
            userBeanService.insert(UserBean.builder()
                    .empNo(travelBean.getLegalNo())
                    .empPassword(travelBean.getTravelPassword())
                    .empName(travelBean.getTravelName())
                    .enabled(Objects.equals(travelBean.getLegalState(), "1") ? 1 : 0)
                    .empSalt(salt)
                    .build());
            userRoleBeanService.insert(UserRoleBean.builder().empNo(travelBean.getLegalNo()).roleId(8).build());
        }catch (Exception e){
            return R.error("该工商号已经注册");
        }
        return R.ok("添加成功");
    }

    @PostMapping("/update")
    public R update(@RequestBody TravelBean travelBean){
        try{
            int i = travelBeanService.updateByPrimaryKey(travelBean);
            if(i == 0) return R.error("不能修改工商号");
            userBeanService.isFreezeEmp(Objects.equals(travelBean.getLegalState(), "1") ? 1 : 0, travelBean.getLegalNo());
        }catch (Exception e){
            return R.error("修改失败，请联系管理员");
        }
        return R.ok("修改成功");
    }

    @DeleteMapping("/delete")
    public R delete(@RequestParam String legalNo){
        try{
            travelBeanService.deleteByPrimaryKey(legalNo);
            userRoleBeanService.deleteByEmpNo(legalNo);
            userBeanService.deleteByPrimaryKey(legalNo);
        }catch (Exception e){
            return R.error("删除失败，请联系管理员");
        }
        return R.ok("删除成功");
    }

    @PutMapping("/enable")
    public R enable(@RequestParam String legalState, String legalNo){
        try{
            travelBeanService.isFreeze(legalState, legalNo);
            userBeanService.isFreezeEmp(Objects.equals(legalState, "1") ? 1 : 0, legalNo);
        }catch (Exception e){
            return R.error("操作失败");
        }
        if(Objects.equals(legalState, "1")){
            return R.ok("恢复正常");
        }else{
            return R.ok("冻结成功");
        }
    }
}
