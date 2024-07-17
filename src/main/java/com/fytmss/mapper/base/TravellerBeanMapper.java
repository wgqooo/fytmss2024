package com.fytmss.mapper.base;

import com.fytmss.beans.base.TravellerBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
@author wgq
@create 2024/6/7-周五 15:13
*/
@Mapper
public interface TravellerBeanMapper {

    List<TravellerBean> queryTravellersInfo();

    int deleteByPrimaryKey(String passportno);

    int insert(TravellerBean record);

    int insertSelective(TravellerBean record);

    TravellerBean selectByPrimaryKey(String passportno);

    int updateByPrimaryKeySelective(TravellerBean record);

    int updateByPrimaryKey(TravellerBean record);

    void insertByBatch(TravellerBean travellerBean);

    List<TravellerBean> queryTravellersInfoByCondition(String passportNo, String trName);

    List<TravellerBean> queryTravellersInfoByParams(String passportNo, String trName);
}