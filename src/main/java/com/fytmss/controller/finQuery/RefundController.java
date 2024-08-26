package com.fytmss.controller.finQuery;

import com.fytmss.pojo.Menu;
import com.fytmss.pojo.MonRecord;
import com.fytmss.pojo.Ticket;
import com.fytmss.service.MenuService;
import com.fytmss.service.MonRecordService;
import com.fytmss.service.OrderInfoService;
import com.fytmss.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

@Controller
public class RefundController {
    @Resource
    private MenuService menuService;
    @Resource
    private TicketService ticketService;
    @Resource
    private OrderInfoService orderInfoService;
    @Autowired
    private MonRecordService monRecordService;

    @RequestMapping("/finQuery/refund")
    public String finQueryRefundView(ModelMap modelMap) {
        List<Menu> menus = menuService.getMenusByEmpNo();
        modelMap.put("menus", menus);

        List<Ticket> ticketReturn = ticketService.find_all_tickets_by_state("退票");
        // List<Ticket> ticketUpdate = ticketService.find_all_tickets_by_state("改签");
        // List<Ticket> tickets = ticketService.find_all_tickets_by_state_two("退票", "改签");
        // ArrayList<Ticket> tickets = new ArrayList<>();
        // tickets.addAll(ticketReturn);
        // tickets.addAll(ticketUpdate);
        for (Ticket t : ticketReturn) {
            if (t.getOrderNo() != null) {
                // 这里属性替换了
                t.setTelTempNumber(orderInfoService.getUserId(t.getOrderNo()));
            } else {
                t.setTelTempNumber("");
            }
        }

        modelMap.put("tickets", ticketReturn);
        return "finQuery_refund";
    }

    @RequestMapping("/finQuery/update")
    public String finQueryUpdateView(ModelMap modelMap) {
        List<Menu> menus = menuService.getMenusByEmpNo();
        modelMap.put("menus", menus);

        List<Ticket> ticketUpdate = ticketService.find_all_tickets_by_state("改签");
        // 针对改签之后再退票的情况，进行修改
        List<Ticket> ticketReturn = ticketService.find_all_tickets_by_state("退票");
        for (Ticket t: ticketReturn) {
            if (t.getOldTickCost() != 0F) {
                ticketUpdate.add(t);
            }
        }

        ticketUpdate.sort(Comparator.comparing(Ticket::getStateDate));

        for (Ticket t : ticketUpdate) {
            // 售票室进行退款
            MonRecord monRecord1 = monRecordService.findMonRecordById(t.getTickNo() + "3");
            if (monRecord1 != null) {
                t.setRefundAndDifference("-" + monRecord1.getTrReMoney());
            }
            // 旅客进行补钱
            MonRecord monRecord2 = monRecordService.findMonRecordById(t.getTickNo() + "4");
            if (monRecord2 != null) {
                if (!Character.isDigit(String.valueOf(monRecord2.getTrReMoney()).charAt(0))) {
                    t.setRefundAndDifference("+" + String.valueOf(monRecord2.getTrReMoney()).substring(1));
                } else {
                    t.setRefundAndDifference(String.valueOf(monRecord2.getTrReMoney()));
                }

            }
        }

        for (Ticket t : ticketUpdate) {
            if (t.getOrderNo() != null) {
                // 这里属性替换了
                t.setTelTempNumber(orderInfoService.getUserId(t.getOrderNo()));
            } else {
                t.setTelTempNumber("");
            }
        }

        modelMap.put("tickets", ticketUpdate);
        return "finQuery_update";
    }


}
