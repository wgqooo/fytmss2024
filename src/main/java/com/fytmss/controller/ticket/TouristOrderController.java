package com.fytmss.controller.ticket;

import com.fytmss.beans.ticket.TicketBean;
import com.fytmss.common.utils.DateUtils;
import com.fytmss.common.utils.R;
import com.fytmss.service.ticket.TicketBeanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;

/**
 * @author wgq
 * @create 2024/6/30-周日 8:52
 */
@RestController
@RequestMapping("/ticket/touristOrder")
public class TouristOrderController {

    @Resource
    private TicketBeanService ticketBeanService;

    @GetMapping("/list")
    public R list(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        String travelType = params.get("travelType");
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        String queryParams = params.get("queryParams");
        PageInfo<TicketBean> tickets = ticketBeanService.findAllTouristTicketsByPageS(index, size, travelType, startDate, endDate, queryParams);
        R r = new R();
        r.put("page", tickets);
        return r;
    }
}
