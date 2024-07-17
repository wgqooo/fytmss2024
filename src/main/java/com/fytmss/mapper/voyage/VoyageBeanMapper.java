package com.fytmss.mapper.voyage;

import com.fytmss.beans.form.VoyDateNoForm;
import com.fytmss.beans.voyage.VoyageBean;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
@author wgq
@create 2024/6/14-周五 14:45
*/
@Mapper
public interface VoyageBeanMapper {

    int deleteByPrimaryKey(@Param("voyDate") String voyDate, @Param("voyNo") String voyNo);

    int insert(VoyageBean record);

    int insertSelective(VoyageBean record);

    VoyageBean selectByPrimaryKey(@Param("voyDate") String voyDate, @Param("voyNo") String voyNo);

    int updateByPrimaryKeySelective(VoyageBean record);

    int updateByPrimaryKey(VoyageBean record);

    List<VoyageBean> queryVoyagesInfo(String startDate, String endDate);

    List<VoyageBean> getAllVoyages();

    List<String> getAllVoyNo();

    int alterVoyState(String voyDate, String voyNo, Integer voyState);

    List<VoyageBean> getAllVoyagesByDate(String date);

    int insertVoyagesByBatch(List<VoyageBean> voyages);

    int delBatchVoys(List<VoyDateNoForm> forms);

    List<VoyageBean> getAllVoyagesBetweenDate(Integer startPort, String startDate, String endDate);


    VoyageBean getVoyByParams(String voyNo, String date, String time);

    List<VoyageBean> queryCarryInfoByDate(String startDate, String endDate);

    List<VoyageBean> queryCarryDetailsByDate(String date);

    List<VoyageBean> queryRCarryDetailsByDate(String date);
}