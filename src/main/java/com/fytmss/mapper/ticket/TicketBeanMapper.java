package com.fytmss.mapper.ticket;

import com.fytmss.beans.form.TeamForm;
import com.fytmss.beans.ticket.TicketBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
@author wgq
@create 2024/6/22-周六 21:58
*/
@Mapper
public interface TicketBeanMapper {

    int deleteByPrimaryKey(String tickno);

    int insert(TicketBean record);

    int insertSelective(TicketBean record);

    TicketBean selectByPrimaryKey(String tickno);

    int updateByPrimaryKeySelective(TicketBean record);

    int updateByPrimaryKey(TicketBean record);

    List<TicketBean> queryTouristTicketsInfo(String travelType, String startDate, String endDate, String queryParams);


    int insertTouristTicketsByBatch(List<TicketBean> tickets);

    Integer queryTicket(String passportNo, String voyTime);


    List<TeamForm> queryCTeamByExit(String startDate, String endDate);

    List<TeamForm> queryCTeamByEntry(String startDate, String endDate);

    List<TeamForm> queryCTeam(String startDate, String endDate);

    List<TeamForm> queryETeamByExit(String startDate, String endDate);

    List<TeamForm> queryETeamByEntry(String startDate, String endDate);

    List<TeamForm> queryETeam(String startDate, String endDate);

    List<TicketBean> selectByTeamNo(String teamNo);

    List<TicketBean> selectByStartDate(String startDate, String state, String startSeat, String returnSeat);

    List<TicketBean> selectByTickDate(String tickDate, String state, String startSeat, String returnSeat);
}