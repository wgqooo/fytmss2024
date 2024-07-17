package com.fytmss.mapper.base;

import com.fytmss.beans.base.UserBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
@author wgq
@create 2024/5/17-周五 14:14
*/
@Mapper
public interface UserBeanMapper {


    int updateEnabled(Integer isEnabled, String empNo);
    List<UserBean> queryUsersInfo();

    int deleteByPrimaryKey(String empNo);

    int insert(UserBean record);

    int insertSelective(UserBean record);

    UserBean selectByPrimaryKey(String empNo);

    int updateByPrimaryKeySelective(UserBean record);

    int updateByPrimaryKey(UserBean record);

}