package com.fytmss.controller.finQuery;

import com.alibaba.fastjson.JSONObject;
import com.fytmss.pojo.Menu;
import com.fytmss.pojo.MonRecord;
import com.fytmss.service.MenuService;
import com.fytmss.service.MonRecordService;
import com.fytmss.service.TravelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PrestoreController {
    @Resource
    private MenuService menuService;
    @Resource
    private MonRecordService monRecordService;
    @Resource
    private TravelService travelService;

    @RequestMapping("/finQuery/prestore")
    public String prestoreView(ModelMap modelMap){
        List<Menu> menus = menuService.getMenusByEmpNo();
        modelMap.put("menus",menus);

        return "finQuery_prestore.html";
    }

    @ResponseBody
    @RequestMapping("/finQuery/prestore/search")
    public JSONObject finQuerySearch(HttpServletRequest request){
        String startT = request.getParameter("startTime");
        String endT = request.getParameter("endTime");
        Integer sEcho = Integer.valueOf(request.getParameter("sEcho"));// 记录操作的次数 每次加1
        Integer iDisplayStart = Integer.valueOf(request.getParameter("iDisplayStart"));// 起始
        Integer iDisplayLength = Integer.valueOf(request.getParameter("iDisplayLength"));// 每页显示的size
        List<MonRecord> monRecords = monRecordService.findPrestoreByDate1(startT,endT,0,iDisplayStart, iDisplayLength);
        int totalRecord = monRecordService.findPrestoreByDate(startT,endT,0).size();
        int cnt = 1;
        for(MonRecord re : monRecords){
            re.setTrLegalName(travelService.findTravel(re.getTrLegalNo()).getTravelName());
            re.setIndex(iDisplayStart+cnt);
            cnt++;
        }
        JSONObject jsonObject = new JSONObject();
        int initEcho = sEcho + 1;
        jsonObject.put("sEcho", initEcho);
        jsonObject.put("iTotalRecords", totalRecord);
        jsonObject.put("iTotalDisplayRecords", totalRecord);
        jsonObject.put("aaData", monRecords);
        return jsonObject;
    }

}
