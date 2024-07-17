package com.fytmss.controller.ticket;

import com.fytmss.beans.ticket.TicketBean;
import com.fytmss.common.utils.DateUtils;
import com.fytmss.common.utils.R;
import com.fytmss.service.ticket.TicketBeanService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author wgq
 * @create 2024/6/27-周四 9:25
 */
@RestController
@RequestMapping("/ticket/teamBook")
public class TeamBookController {

    @Resource
    private TicketBeanService ticketBeanService;

    @PostMapping("/addTickets")
    public R addTickets(@RequestBody List<TicketBean> tickets){
        System.out.println(tickets);
        String dateTime = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
        for (TicketBean ticket : tickets) {
            ticket.setTickDate(dateTime);
            ticket.setStateDate(dateTime);
            ticket.setReturnStateDate(dateTime);
        }
        try{
            ticketBeanService.insertTicketsByBatch(tickets);
        }catch (Exception e){
            return R.error("创建失败，请联系管理员");
        }
        return R.ok("订单生成成功");
    }
}
