package com.fytmss.controller.ticket;

import com.fytmss.beans.form.TeamForm;
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
import java.util.List;
import java.util.Objects;

/**
 * @author wgq
 * @create 2024/7/3-周三 15:43
 */
@RestController
@RequestMapping("/ticket/teamOrder")
public class TeamOrderController {

    @Resource
    private TicketBeanService ticketBeanService;
    @Resource
    private VoyageBeanService voyageBeanService;

    @GetMapping("/list")
    public R list(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        String country = params.get("country");
        String exitEntry = params.get("exitEntry");
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        PageInfo<TeamForm> teams = null;
        if(Objects.equals(country, "中团")){
            if(Objects.equals(exitEntry, "出境")) teams =  ticketBeanService.findCTeamByExit(index, size, startDate, endDate);
            else if(Objects.equals(exitEntry, "入境")) teams = ticketBeanService.findCTeamByEntry(index, size, startDate, endDate);
            else teams = ticketBeanService.findCTeam(index, size, startDate, endDate);
        }else{
            if(Objects.equals(exitEntry, "出境")) teams = ticketBeanService.findETeamByExit(index, size, startDate, endDate);
            else if(Objects.equals(exitEntry, "入境")) teams = ticketBeanService.findETeamByEntry(index, size, startDate, endDate);
            else teams = ticketBeanService.findETeam(index, size, startDate, endDate);
        }
        R r = new R();
        r.put("page", teams);
        return r;
    }

    @GetMapping("/orderInfo")
    public R orderInfo(String teamNo){
        List<TicketBean> ticketList = ticketBeanService.selectByTeamNo(teamNo);
        TicketBean ticketBean = ticketList.get(0);
        String startVoyNo = ticketBean.getStartVoyNo();
        String[] start_date_time = ticketBean.getStartDate().split(" ");
        String returnVoyNo = ticketBean.getReturnVoyNo();
        String[] return_date_time = ticketBean.getReturnDate().split(" ");
        VoyageBean startVoy = voyageBeanService.selectByParams(startVoyNo, start_date_time[0], start_date_time[1]);
        VoyageBean returnVoy = voyageBeanService.selectByParams(returnVoyNo, return_date_time[0], return_date_time[1]);
        R r = new R();
        r.put("startVoy", startVoy);
        r.put("returnVoy", returnVoy);
        r.put("page", ticketList);
        return r;
    }

}
