package com.fytmss.mapper.base;

import com.fytmss.beans.base.TravelBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
@author wgq
@create 2024/6/1-周六 14:50
*/
@Mapper
public interface TravelBeanMapper {

    int isFreeze(String legalState, String legalNo);
    List<TravelBean> queryTravelsInfo();

    int deleteByPrimaryKey(String legalno);

    int insert(TravelBean record);

    int insertSelective(TravelBean record);

    TravelBean selectByPrimaryKey(String legalno);

    int updateByPrimaryKeySelective(TravelBean record);

    int updateByPrimaryKey(TravelBean record);



}