package com.fytmss.controller.base;

import com.fytmss.beans.base.TravellerBean;
import com.fytmss.beans.base.UserBean;
import com.fytmss.common.utils.R;
import com.fytmss.common.utils.ShiroUtils;
import com.fytmss.service.base.TravellerBeanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

/**
 * @author wgq
 * @create 2024/6/7-周五 15:31
 */
@RestController
@RequestMapping("/base/traveller")
public class TravellerController {

    @Resource
    private TravellerBeanService travellerBeanService;

    @GetMapping("/list")
    public R list(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        PageInfo<TravellerBean> travellers = travellerBeanService.findAllTravellersByPageS(index, size);
        R r = new R();
        r.put("page", travellers);
        return r;
    }

    @GetMapping("/listByCondition")
    public R listByCondition(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        String passportNo = params.get("passportNo");
        String trName = params.get("trName");
        String startDate = params.get("startDate");
        String returnDate = params.get("returnDate");
        PageInfo<TravellerBean> travellers = travellerBeanService.findAllTravellersByPageAndCondition(index, size, passportNo, trName);
        R r = new R();
        //过滤航次时间冲突旅客(这里加大了限制，只要是买了一个时间就不能再买相同时间
        if(returnDate == null){
            for (TravellerBean travellerBean : travellers.getList()) {
                travellerBean.setHasTicket(travellerBeanService.judgeHasTicket(travellerBean.getPassportNo(), startDate));
            }
        }else{
            for (TravellerBean travellerBean : travellers.getList()) {
                travellerBean.setHasTicket(travellerBeanService.judgeHasTicket(travellerBean.getPassportNo(), startDate) |
                        travellerBeanService.judgeHasTicket(travellerBean.getPassportNo(), returnDate));
            }
        }
        r.put("page", travellers);
        return r;
    }



    @PostMapping("/save")
    public R save(@RequestBody TravellerBean travellerBean){
        try{
            //数据库设置了填写插入的时间
            //travellerBean.setInSertDate(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
            travellerBean.setTrAuthen(3);
            travellerBean.setIsDel(0);
            travellerBeanService.insert(travellerBean);
        }catch (Exception e){
            return R.error();
        }
        return R.ok("添加成功");
    }

    @PostMapping("/update")
    public R update(@RequestBody TravellerBean travellerBean){
        try{
            int i = travellerBeanService.updateByPrimaryKey(travellerBean);
            if(i == 0) return R.error("不能修改护照号");
        }catch (Exception e){
            return R.error("修改失败，请联系管理员");
        }
        return R.ok("修改成功");
    }

    @DeleteMapping("/delete")
    public R delete(@RequestParam String passportNo){
        try{
            travellerBeanService.deleteByPrimaryKey(passportNo);
        }catch (Exception e){
            return R.error("删除失败，请联系管理员");
        }
        return R.ok("删除成功");
    }


    /**
     *
     * @param travellerBeans excel表单导入的旅客数据（前端已经作出处理）
     * @return
     */
    @PostMapping("/saveByBatch")
    public R saveByBatch(@RequestBody List<TravellerBean> travellerBeans){
        //判断当前用户是旅行社或售票室
        UserBean user = ShiroUtils.getUser();
        String trTravel = "";
        int count = 0;
        if(user.getEmpBirthday() == null){
            trTravel = user.getEmpNo(); //旅行社
        }else{
            trTravel = "000000"; //售票室
        }
        for(TravellerBean travellerBean : travellerBeans){
            travellerBean.setTrTravel(trTravel);
            travellerBean.setTrAuthen(3);
            travellerBean.setIsDel(0);
        }
        try{
            count = travellerBeanService.insertTravellersBatch(travellerBeans);
        }catch (Exception e) {
            return R.error("部分数据有误!");
        }
        return R.ok("成功添加" + count + "条数据");
    }

}
