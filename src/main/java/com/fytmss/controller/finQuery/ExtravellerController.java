package com.fytmss.controller.finQuery;

import com.fytmss.pojo.Extraveller;
import com.fytmss.pojo.Menu;
import com.fytmss.service.ExtravellerService;
import com.fytmss.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExtravellerController {

    @Resource
    private MenuService menuService;
    @Resource
    private ExtravellerService extravellerService;


    @RequestMapping("/finQuery/extraveller")
    public String extravellerView(ModelMap modelMap){
        List<Menu> menus = menuService.getMenusByEmpNo();
        modelMap.put("menus",menus);

        return "finQuery_extraveller.html";
    }

    @ResponseBody
    @RequestMapping("/finQuery/extraveller/search/out")
    public ArrayList<Extraveller> extravellerSearchOut(HttpServletRequest request){
        String startT = request.getParameter("startT");
        String endT = request.getParameter("endT");
        String findType = request.getParameter("findType");
        if(findType.equals("Month"))
            return extravellerService.findExtrByMonth(startT,endT,"出境");
        else if (findType.equals("Year")) {
            return extravellerService.findExtrByYear(startT,endT,"出境");
        } else {
            return extravellerService.findExtrByDay(startT,endT,"出境");
        }
    }

    @ResponseBody
    @RequestMapping("/finQuery/extraveller/search/in")
    public ArrayList<Extraveller> extravellerSearchIn(HttpServletRequest request){
        String startT = request.getParameter("startT");
        String endT = request.getParameter("endT");
        String findType = request.getParameter("findType");
        if(findType.equals("Month"))
            return extravellerService.findExtrByMonth(startT,endT,"入境");
        else if (findType.equals("Year")) {
            return extravellerService.findExtrByYear(startT,endT,"入境");
        } else {
            return extravellerService.findExtrByDay(startT,endT,"入境");
        }
    }
}
