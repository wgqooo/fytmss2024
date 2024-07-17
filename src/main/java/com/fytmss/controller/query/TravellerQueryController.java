package com.fytmss.controller.query;

import com.fytmss.beans.base.TravellerBean;
import com.fytmss.common.utils.R;
import com.fytmss.service.base.TravellerBeanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author wgq
 * @create 2024/7/6-周六 3:58
 */
@RestController
@RequestMapping("/query/travellerQuery")
public class TravellerQueryController {

    @Resource
    private TravellerBeanService travellerBeanService;

    @GetMapping("/travellerInfo")
    public R travellerInfo(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        String passportNo = params.get("passportNo");
        String trName = params.get("trName");
        PageInfo<TravellerBean> travellerList =  travellerBeanService.selectPageByParams(index, size, passportNo, trName);
        R r = new R();
        r.put("page", travellerList);
        return r;
    }
}
