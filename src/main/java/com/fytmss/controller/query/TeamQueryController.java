package com.fytmss.controller.query;

import com.fytmss.beans.ticket.TicketBean;
import com.fytmss.beans.voyage.VoyageBean;
import com.fytmss.common.utils.R;
import com.fytmss.service.ticket.TicketBeanService;
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
 * @create 2024/7/6-周六 0:02
 */
@RestController
@RequestMapping("/query/teamQuery")
public class TeamQueryController {

    @Resource
    private TicketBeanService ticketBeanService;

    @Resource
    private VoyageBeanService voyageBeanService;

    @GetMapping("/teamInfo")
    public R teamInfo(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        String teamNo = params.get("teamNo");
        PageInfo<TicketBean> ticketList = ticketBeanService.selectPageByTeamNo(index, size, teamNo);
        if(ticketList.getList().isEmpty()){
            return R.error("不存在该团号");
        }
        TicketBean ticketBean = ticketList.getList().get(0);
        String startVoyNo = ticketBean.getStartVoyNo();
        String[] start_date_time = ticketBean.getStartDate().split(" ");
        VoyageBean startVoy = voyageBeanService.selectByParams(startVoyNo, start_date_time[0], start_date_time[1]);
        R r = new R();
        if(ticketBean.getReturnVoyNo() != null){
            String returnVoyNo = ticketBean.getReturnVoyNo();
            String[] return_date_time = ticketBean.getReturnDate().split(" ");
            VoyageBean returnVoy = voyageBeanService.selectByParams(returnVoyNo, return_date_time[0], return_date_time[1]);
            r.put("returnVoy", returnVoy);
        }
        r.put("startVoy", startVoy);
        r.put("page", ticketList);
        return r;
    }
}
