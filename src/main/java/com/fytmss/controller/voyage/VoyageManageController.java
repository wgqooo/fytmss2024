package com.fytmss.controller.voyage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fytmss.beans.form.VoyDateNoForm;
import com.fytmss.beans.form.VoyagesForm;
import com.fytmss.beans.voyage.VoyageBean;
import com.fytmss.common.utils.DateUtils;
import com.fytmss.common.utils.R;
import com.fytmss.service.voyage.VoyageBeanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wgq
 * @create 2024/6/16-周日 13:15
 */
@RestController
@RequestMapping("/voyage/voyageManage")
public class VoyageManageController {

    @Resource
    private VoyageBeanService voyageBeanService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ObjectMapper objectMapper;



    @GetMapping("/list")
    public R list(@RequestParam HashMap<String, String> params){
        int index = Integer.parseInt(params.get("index"));
        int size = Integer.parseInt(params.get("size"));
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        //List<String> datesInRange = DateUtils.getDatesInRange(startDate, endDate);
        PageInfo<VoyageBean> voyages = voyageBeanService.findAllVoyagesByPageS(index, size, startDate, endDate);
        R r = new R();
        r.put("page", voyages);
        return r;
    }

    @GetMapping("/allBetweenDate")
    public R list(Integer startPort, String startDate, String endDate){
        //todo 这里貌似可以优化，从redis里面读取每条航次的剩余作为数量可以更快
        List<VoyageBean> voyages = voyageBeanService.findAllVoyagesBetweenDate(startPort, startDate, endDate);
        R r = new R();
        r.put("voyages", voyages);
        return r;
    }


    @PostMapping("/save")
    public R save(@RequestBody VoyageBean voyageBean){
        try{
            if(Objects.equals(voyageBean.getReShipNo(), "")) voyageBean.setReShipNo(voyageBean.getShipNo());
            voyageBeanService.insert(voyageBean);
            // 构建 Redis 键
            String voyKey = "voyage:" + voyageBean.getVoyDate() + ":" + voyageBean.getVoyNo();
            //查询该航次的船型（座位数量分布）
            VoyageBean voyDetail = voyageBeanService.selectByParams(voyageBean.getVoyNo(), voyageBean.getVoyDate(), voyageBean.getStartTime());
            // 使用来 stringRedisTemplate.opsForHash()存储座位数据
            stringRedisTemplate.opsForHash().put(voyKey, "vipLeft", String.valueOf(voyDetail.getVipSeat() - voyDetail.getVipRes()));
            stringRedisTemplate.opsForHash().put(voyKey, "firLeft", String.valueOf(voyDetail.getFirSeat() - voyDetail.getFirRes()));
            stringRedisTemplate.opsForHash().put(voyKey, "secLeft", String.valueOf(voyDetail.getSecSeat() - voyDetail.getSecRes()));
            //设置买票的截止时间（也就是redis中该voy的存活时间）
            String dateTimeStr = voyDetail.getVoyDate() + " " + voyDetail.getStartTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date expireDate = dateFormat.parse(dateTimeStr);
            stringRedisTemplate.expireAt(voyKey, expireDate);
        }catch (Exception e){
            return R.error("添加航次失败，请联系管理员");
        }
        return R.ok("添加成功");
    }

    @PostMapping("/update")
    public R update(@RequestBody VoyageBean voyageBean){
        try{
            //不涉及主键属性的修改
            voyageBeanService.updateByPrimaryKey(voyageBean);
        }catch (Exception e){
            return R.error("修改失败，请联系管理员");
        }
        return R.ok("修改成功");
    }

    @DeleteMapping("/delete")
    public R delete(@RequestParam String voyDate, @RequestParam String voyNo){
        try{
            voyageBeanService.deleteByPrimaryKey(voyDate, voyNo);
        }catch (Exception e){
            return R.error("删除失败，请联系管理员");
        }
        return R.ok("删除成功");
    }

    @PutMapping("/state")
    public R state(String voyDate, String voyNo, Integer voyState){
        try{
            voyageBeanService.changeVoyState(voyDate, voyNo, voyState);
        }catch (Exception e){
            return R.error("操作失败，请联系管理员");
        }
        if(voyState == 1){
            return R.ok("船次已生效");
        }else if(voyState == 0){
            return R.ok("船次已冻结");
        }else return R.ok("船次已停运");
    }

    @PutMapping("/stateBatchChange")
    public R stateBatchChange(String voyDate, String voyNo, Integer voyState){
        try{
            voyageBeanService.changeVoyState(voyDate, voyNo, voyState);
        }catch (Exception e){
            return R.error("操作失败，请联系管理员");
        }
        if(voyState == 1){
            return R.ok("船次已生效");
        }else if(voyState == 0){
            return R.ok("船次已冻结");
        }else return R.ok("船次已停运");
    }

    @GetMapping("/listAllVoyages")
    public R listAllShipNames(){
        List<VoyageBean> voyages = voyageBeanService.findAllVoyages();
        R r = new R();
        r.put("voyages", voyages);
        return r;
    }

    @GetMapping("/listAllVoyNo")
    public R listAllVoyNo(){
        List<String> voyNos = voyageBeanService.findAllVoyNo();
        R r = new R();
        r.put("voyNos", voyNos);
        return r;
    }

    @GetMapping("/VoysByDate")
    public R voysByDate(String date){
        List<VoyageBean> voyages = voyageBeanService.findAllVoyagesByDate(date);
        R r = new R();
        r.put("voyages", voyages);
        return r;
    }

    @PostMapping("/addVoyages")
    public R addVoyages(@RequestBody VoyagesForm voyagesForm){
        List<VoyageBean> voyages = voyagesForm.getVoyages();
        String startDate = voyagesForm.getStartDate();
        String endDate = voyagesForm.getEndDate();
        List<String> datesInRange = DateUtils.getDatesInRange(startDate, endDate);
        List<String> invalidDate = new ArrayList<>();
        for (String date : datesInRange) {
            for (VoyageBean voyage : voyages) {
                voyage.setVoyDate(date);
            }
            try{
                voyageBeanService.insertVoyagesByBatch(voyages);
            }catch (Exception e){
                invalidDate.add(date);
            }
        }
        if(!invalidDate.isEmpty()){
            R r = new R();
            r.put("code", 500);
            r.put("invalidDate", invalidDate);
            return r;
        }
        return R.ok("添加成功");
    }

    @DeleteMapping("/delVoyages")
    public R delVoyages(@RequestBody List<VoyDateNoForm> forms){
        int i = 0;
        try{
            i = voyageBeanService.deleteVoyagesByBatch(forms);
        }catch (Exception e){
            return R.error("删除失败，请联系管理员");
        }
        return R.ok(i + "条航次已全部删除");
    }
}
