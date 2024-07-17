package com.fytmss.controller.finance;

import com.fytmss.beans.voyage.VoyageBean;
import com.fytmss.common.utils.R;
import com.fytmss.service.voyage.VoyageBeanService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author wgq
 * @create 2024/7/14-周日 15:15
 */
@RestController
@RequestMapping("/finance/carryDetails")
public class CarryDetailsController {

    @Resource
    private VoyageBeanService voyageBeanService;

    @GetMapping("/list")
    public R list(@RequestParam(value = "date") String date){
        List<VoyageBean> carryDetails = voyageBeanService.selectCarryDetailsByDate(date);
        List<VoyageBean> carryRDetails = voyageBeanService.selectRCarryDetailsByDate(date);
        R r = new R();
        r.put("carryDetails", carryDetails);
        r.put("carryRDetails", carryRDetails);
        return r;
    }
}
