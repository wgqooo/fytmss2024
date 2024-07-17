package com.fytmss.controller.query;

import com.fytmss.beans.ticket.TicketBean;
import com.fytmss.common.utils.R;
import com.fytmss.service.ticket.TicketBeanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author wgq
 * @create 2024/7/6-周六 0:01
 */
@RestController
@RequestMapping("/query/ticketQuery")
public class TicketQueryController {

    @Resource
    private TicketBeanService ticketBeanService;

    @GetMapping("/ticketInfo")
    public R teamInfo(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        String dateType = params.get("dateType");
        String date = params.get("date");
        String state = params.get("state");
        String startSeat = params.get("startSeat");
        String returnSeat = params.get("returnSeat");
        if(Objects.equals(state, "3")) state = "";
        else if(Objects.equals(state, "0")) state = "未检票";
        else if(Objects.equals(state, "1")) state = "退票";
        else  state = "取消";
        if(Objects.equals(startSeat, "3")) startSeat = "";
        else if(Objects.equals(startSeat, "0")) startSeat = "VIP座";
        else if(Objects.equals(startSeat, "1")) startSeat = "一等座";
        else  startSeat = "二等座";
        if(Objects.equals(returnSeat, "3")) returnSeat = "";
        else if(Objects.equals(returnSeat, "0")) returnSeat = "VIP座";
        else if(Objects.equals(returnSeat, "1")) returnSeat = "一等座";
        else  returnSeat = "二等座";
        PageInfo<TicketBean> ticketList = null;
        if(Objects.equals(dateType, "0")){
            ticketList = ticketBeanService.selectPageByStartDate(index, size, date, state, startSeat, returnSeat);
        }else{
            ticketList = ticketBeanService.selectPageByTickDate(index, size, date, state, startSeat, returnSeat);
        }
        R r = new R();
        r.put("page", ticketList);
        return r;
        //return null;
    }
}
