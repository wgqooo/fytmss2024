package com.fytmss.service.base;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;



import com.fytmss.beans.base.ShipBean;
import com.fytmss.mapper.base.ShipBeanMapper;

import java.util.List;

/**
@author wgq
@create 2024/5/30-周四 23:12
*/
@Service
public class ShipBeanService{

    @Resource
    private ShipBeanMapper shipBeanMapper;

    /**
     *
     * @param pageNum
     * @param pageSize
     * @return 分页查找船只信息
     */
    public PageInfo<ShipBean> findAllShipByPageS(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ShipBean> ships = shipBeanMapper.queryShipsInfo();
        return new PageInfo<>(ships);
    }

    
    public int deleteByPrimaryKey(String shipno) {
        return shipBeanMapper.deleteByPrimaryKey(shipno);
    }

    
    public int insert(ShipBean record) {
        return shipBeanMapper.insert(record);
    }

    
    public int insertSelective(ShipBean record) {
        return shipBeanMapper.insertSelective(record);
    }

    
    public ShipBean selectByPrimaryKey(String shipno) {
        return shipBeanMapper.selectByPrimaryKey(shipno);
    }

    
    public int updateByPrimaryKeySelective(ShipBean record) {
        return shipBeanMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(ShipBean record) {
        return shipBeanMapper.updateByPrimaryKey(record);
    }


    public List<ShipBean> findAllShips() {
        return shipBeanMapper.queryAllShips();
    }
}
