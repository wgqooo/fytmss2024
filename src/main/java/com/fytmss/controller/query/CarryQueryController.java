package com.fytmss.controller.query;

import com.fytmss.beans.voyage.VoyageBean;
import com.fytmss.common.utils.R;
import com.fytmss.service.voyage.VoyageBeanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author wgq
 * @create 2024/7/6-周六 14:59
 */
@RestController
@RequestMapping("/query/carryQuery")
public class CarryQueryController {

    @Resource
    private VoyageBeanService voyageBeanService;

    @GetMapping("/carryInfo")
    public R list(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        System.out.println(startDate + ", " + endDate);
        PageInfo<VoyageBean> voyageList = voyageBeanService.selectCarryInfoByDate(index, size, startDate, endDate);
        R r = new R();
        r.put("page", voyageList);
        return r;
    }
}
