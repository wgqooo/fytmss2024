package com.fytmss.service.voyage;

import com.fytmss.beans.base.TravellerBean;
import com.fytmss.beans.form.VoyDateNoForm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.fytmss.beans.voyage.VoyageBean;
import java.util.List;

import com.fytmss.mapper.voyage.VoyageBeanMapper;
/**
@author wgq
@create 2024/6/14-周五 14:45
*/
@Service
public class VoyageBeanService{

    @Autowired
    private VoyageBeanMapper voyageBeanMapper;


    public PageInfo<VoyageBean> findAllVoyagesByPageS(int pageNum, int pageSize, String startDate, String endDate) {
        PageHelper.startPage(pageNum, pageSize);
        List<VoyageBean> travels = voyageBeanMapper.queryVoyagesInfo(startDate, endDate);
        return new PageInfo<>(travels);
    }

    public int deleteByPrimaryKey(String voydate,String voyno) {
        return voyageBeanMapper.deleteByPrimaryKey(voydate,voyno);
    }

    
    public int insert(VoyageBean record) {
        return voyageBeanMapper.insert(record);
    }

    
    public int insertSelective(VoyageBean record) {
        return voyageBeanMapper.insertSelective(record);
    }

    
    public VoyageBean selectByPrimaryKey(String voydate,String voyno) {
        return voyageBeanMapper.selectByPrimaryKey(voydate,voyno);
    }

    
    public int updateByPrimaryKeySelective(VoyageBean record) {
        return voyageBeanMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(VoyageBean record) {
        return voyageBeanMapper.updateByPrimaryKey(record);
    }

    public List<VoyageBean> findAllVoyages() {
        return voyageBeanMapper.getAllVoyages();
    }

    public List<String> findAllVoyNo() {
        return voyageBeanMapper.getAllVoyNo();
    }

    public int changeVoyState(String voyDate, String voyNo, Integer voyState) {
        return voyageBeanMapper.alterVoyState(voyDate, voyNo, voyState);
    }

    public List<VoyageBean> findAllVoyagesByDate(String date) {
        return voyageBeanMapper.getAllVoyagesByDate(date);
    }

    public int insertVoyagesByBatch(List<VoyageBean> voyages) {
        return voyageBeanMapper.insertVoyagesByBatch(voyages);
    }

    public int deleteVoyagesByBatch(List<VoyDateNoForm> forms) {
        return voyageBeanMapper.delBatchVoys(forms);
    }

    public List<VoyageBean> findAllVoyagesBetweenDate(Integer startPort, String startDate, String endDate) {
        return voyageBeanMapper.getAllVoyagesBetweenDate(startPort, startDate, endDate);
    }

    public VoyageBean selectByParams(String voyNo, String date, String time) {
        return voyageBeanMapper.getVoyByParams(voyNo, date, time);
    }

    public PageInfo<VoyageBean> selectCarryInfoByDate(int pageNum, int pageSize, String startDate, String endDate) {
        PageHelper.startPage(pageNum, pageSize);
        List<VoyageBean> travels = voyageBeanMapper.queryCarryInfoByDate(startDate, endDate);
        return new PageInfo<>(travels);
    }

    public List<VoyageBean> selectCarryDetailsByDate(String date) {
        return voyageBeanMapper.queryCarryDetailsByDate(date);
    }

    public List<VoyageBean> selectRCarryDetailsByDate(String date) {
        return voyageBeanMapper.queryRCarryDetailsByDate(date);
    }
}
