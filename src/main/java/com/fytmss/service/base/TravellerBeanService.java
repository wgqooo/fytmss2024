package com.fytmss.service.base;

import com.fytmss.mapper.ticket.TicketBeanMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;


import com.fytmss.mapper.base.TravellerBeanMapper;
import com.fytmss.beans.base.TravellerBean;

import java.util.List;

/**
@author wgq
@create 2024/6/7-周五 15:13
*/
@Service
public class TravellerBeanService{

    @Resource
    private TravellerBeanMapper travellerBeanMapper;
    @Resource
    private SqlSessionTemplate sqlSessionTemplate;
    @Resource
    private TicketBeanMapper ticketBeanMapper;

    /**
     * excel表格解析的旅客信息批量插入，使用ExecutorType.BATCH插入
     * @return 返回插入成功的数据
     */
    public int insertTravellersBatch(List<TravellerBean> travellerBeans){
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        int batch = 1000;
        int count = 0;
        for (TravellerBean travellerBean : travellerBeans) {
            travellerBeanMapper.insertByBatch(travellerBean);
            count++;
            if(count != 0 && count % batch == 0){
                sqlSession.commit();
            }
        }
        sqlSession.commit();
        return count;
    }

    public PageInfo<TravellerBean> findAllTravellersByPageS(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TravellerBean> travellers = travellerBeanMapper.queryTravellersInfo();
        return new PageInfo<>(travellers);
    }

    public PageInfo<TravellerBean> findAllTravellersByPageAndCondition(int pageNum, int pageSize, String passportNo, String trName) {
        PageHelper.startPage(pageNum, pageSize);
        List<TravellerBean> travellers = travellerBeanMapper.queryTravellersInfoByCondition(passportNo, trName);
        return new PageInfo<>(travellers);
    }
    
    public int deleteByPrimaryKey(String passportno) {
        return travellerBeanMapper.deleteByPrimaryKey(passportno);
    }

    
    public int insert(TravellerBean record) {
        return travellerBeanMapper.insert(record);
    }

    
    public int insertSelective(TravellerBean record) {
        return travellerBeanMapper.insertSelective(record);
    }

    
    public TravellerBean selectByPrimaryKey(String passportno) {
        return travellerBeanMapper.selectByPrimaryKey(passportno);
    }

    
    public int updateByPrimaryKeySelective(TravellerBean record) {
        return travellerBeanMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(TravellerBean record) {
        return travellerBeanMapper.updateByPrimaryKey(record);
    }


    public Integer judgeHasTicket(String passportNo, String voyTime) {
        return ticketBeanMapper.queryTicket(passportNo, voyTime);
    }


    public PageInfo<TravellerBean> selectPageByParams(int pageNum, int pageSize, String passportNo, String trName) {
        PageHelper.startPage(pageNum, pageSize);
        List<TravellerBean> travellers = travellerBeanMapper.queryTravellersInfoByParams(passportNo, trName);
        return new PageInfo<>(travellers);
    }
}
