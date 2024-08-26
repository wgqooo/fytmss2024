package com.fytmss.service.ticket;

import com.fytmss.beans.base.TravellerBean;
import com.fytmss.beans.form.TeamForm;
import com.fytmss.common.utils.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.fytmss.mapper.ticket.TicketBeanMapper;
import com.fytmss.beans.ticket.TicketBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
@author wgq
@create 2024/6/22-周六 21:58
*/
@Service
public class TicketBeanService{

    @Autowired
    private TicketBeanMapper ticketBeanMapper;

    
    public int deleteByPrimaryKey(String tickno) {
        return ticketBeanMapper.deleteByPrimaryKey(tickno);
    }

    
    public int insert(TicketBean record) {
        return ticketBeanMapper.insert(record);
    }

    
    public int insertSelective(TicketBean record) {
        return ticketBeanMapper.insertSelective(record);
    }

    
    public TicketBean selectByPrimaryKey(String tickno) {
        return ticketBeanMapper.selectByPrimaryKey(tickno);
    }

    
    public int updateByPrimaryKeySelective(TicketBean record) {
        return ticketBeanMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(TicketBean record) {
        return ticketBeanMapper.updateByPrimaryKey(record);
    }

    public List<TicketBean> selectByTeamNo(String teamNo){
        return ticketBeanMapper.selectByTeamNo(teamNo);
    }

    public PageInfo<TicketBean> findAllTouristTicketsByPageS(int index, int size, String travelType, String startDate, String endDate, String queryParams) {
        PageHelper.startPage(index, size);
        List<TicketBean> tickets = ticketBeanMapper.queryTouristTicketsInfo(travelType, startDate, endDate, queryParams);
        return new PageInfo<>(tickets);
    }

    public PageInfo<TeamForm> findCTeamByExit(int index, int size, String startDate, String endDate){
        PageHelper.startPage(index, size);
        List<TeamForm> teams = ticketBeanMapper.queryCTeamByExit(startDate, endDate);
        return new PageInfo<>(teams);
    }

    public PageInfo<TeamForm> findCTeamByEntry(int index, int size, String startDate, String endDate){
        PageHelper.startPage(index, size);
        List<TeamForm> teams = ticketBeanMapper.queryCTeamByEntry(startDate, endDate);
        return new PageInfo<>(teams);
    }

    public PageInfo<TeamForm> findCTeam(int index, int size, String startDate, String endDate){
        PageHelper.startPage(index, size);
        List<TeamForm> teams = ticketBeanMapper.queryCTeam(startDate, endDate);
        return new PageInfo<>(teams);
    }

    public PageInfo<TeamForm> findETeamByExit(int index, int size, String startDate, String endDate){
        PageHelper.startPage(index, size);
        List<TeamForm> teams = ticketBeanMapper.queryETeamByExit(startDate, endDate);
        return new PageInfo<>(teams);
    }

    public PageInfo<TeamForm> findETeamByEntry(int index, int size, String startDate, String endDate){
        PageHelper.startPage(index, size);
        List<TeamForm> teams = ticketBeanMapper.queryETeamByEntry(startDate, endDate);
        return new PageInfo<>(teams);
    }

    public PageInfo<TeamForm> findETeam(int index, int size, String startDate, String endDate){
        PageHelper.startPage(index, size);
        List<TeamForm> teams = ticketBeanMapper.queryETeam(startDate, endDate);
        return new PageInfo<>(teams);
    }

//    public PageInfo<TicketBean> findCTeamTicketsByExit(int index, int size, String startDate, String endDate, String queryParams) {
//        PageHelper.startPage(index, size);
//        List<TicketBean> tickets = ticketBeanMapper.queryCTeamTicketsByExit(startDate, endDate, queryParams);
//        return new PageInfo<>(tickets);
//    }

    public int insertTicketsByBatch(List<TicketBean> tickets) {
        return ticketBeanMapper.insertTouristTicketsByBatch(tickets);
    }


    public PageInfo<TicketBean> selectPageByTeamNo(int index, int size, String teamNo) {
        PageHelper.startPage(index, size);
        List<TicketBean> tickets = ticketBeanMapper.selectByTeamNo(teamNo);
        return new PageInfo<>(tickets);
    }


    public PageInfo<TicketBean> selectPageByStartDate(int index, int size, String startDate, String state, String startSeat, String returnSeat) {
        PageHelper.startPage(index, size);
        List<TicketBean> tickets = ticketBeanMapper.selectByStartDate(startDate, state, startSeat, returnSeat);
        return new PageInfo<>(tickets);
    }

    public PageInfo<TicketBean> selectPageByTickDate(int index, int size, String tickDate, String state, String startSeat, String returnSeat) {
        PageHelper.startPage(index, size);
        List<TicketBean> tickets = ticketBeanMapper.selectByTickDate(tickDate, state, startSeat, returnSeat);
        return new PageInfo<>(tickets);
    }
}
