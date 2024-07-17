package com.fytmss.service.base;

import com.fytmss.beans.base.ShipBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.fytmss.mapper.base.TravelBeanMapper;
import com.fytmss.beans.base.TravelBean;

import java.util.List;
import java.util.Objects;

/**
@author wgq
@create 2024/6/1-周六 14:50
*/
@Service
public class TravelBeanService{

    @Resource
    private TravelBeanMapper travelBeanMapper;

    public PageInfo<TravelBean> findAllTravelsByPageS(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TravelBean> travels = travelBeanMapper.queryTravelsInfo();
        return new PageInfo<>(travels);
    }

    
    public int deleteByPrimaryKey(String legalno) {
        return travelBeanMapper.deleteByPrimaryKey(legalno);
    }

    
    public int insert(TravelBean record) {
        return travelBeanMapper.insert(record);
    }

    
    public int insertSelective(TravelBean record) {
        return travelBeanMapper.insertSelective(record);
    }

    
    public TravelBean selectByPrimaryKey(String legalno) {
        return travelBeanMapper.selectByPrimaryKey(legalno);
    }

    
    public int updateByPrimaryKeySelective(TravelBean record) {
        return travelBeanMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(TravelBean record) {
        return travelBeanMapper.updateByPrimaryKey(record);
    }

    public int isFreeze(String legalState, String legalNo) {
        return travelBeanMapper.isFreeze(legalState, legalNo);
    }
}
