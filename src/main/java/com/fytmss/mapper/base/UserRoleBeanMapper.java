package com.fytmss.mapper.base;

import com.fytmss.beans.base.UserRoleBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
@author wgq
@create 2024/5/20-周一 20:50
*/
@Mapper
public interface UserRoleBeanMapper {


    int deleteByEmpNo(String empNo);


    /**
     * 该系统只设置了一个用户对应一个角色
     * @param empNo
     * @return
     */
    UserRoleBean queryByEmpNo(String empNo);


    String queryRoleNameByEmpNo(String empNo);

    /**
     *
     * @param empNo 根据empNo，获取roleId列表
     * @return 一个用户可能有多个角色，用List接收
     */
    List<Integer> queryRoleIdList(Integer empNo);

    int deleteByPrimaryKey(Integer empRoleId);

    int insert(UserRoleBean record);

    int insertSelective(UserRoleBean record);

    UserRoleBean selectByPrimaryKey(Integer empRoleId);

    int updateByPrimaryKeySelective(UserRoleBean record);

    int updateByPrimaryKey(UserRoleBean record);
}