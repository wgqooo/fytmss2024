package com.fytmss.controller.financialController;

import com.fytmss.pojo.Menu;
import com.fytmss.pojo.Voyage;
import com.fytmss.service.MenuService;
import com.fytmss.service.VoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 财务管理--财务审核
 */
@Controller
public class financialController {
    private MenuService menuService;
    private VoyageService voyageService;

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setVoyageService(VoyageService voyageService) {
        this.voyageService = voyageService;
    }

    @RequestMapping("/sys/financial")
    public String shipInfo(ModelMap map) {
        List<Menu> menus = menuService.getMenusByEmpNo();
        map.put("menus", menus);
        String voyDate = null;
        String voyNo = null;
        Voyage voyage = voyageService.findVoyage(voyDate, voyNo);
        map.put("ALLfinancial", voyage);
        return "financial";
    }
}
