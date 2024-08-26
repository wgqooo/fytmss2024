package com.fytmss.mapper.base;

import com.fytmss.beans.base.RoleBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
@author wgq
@create 2024/5/20-周一 20:34
*/
@Mapper
public interface RoleBeanMapper {

    List<RoleBean> queryAllRoles();

    int deleteByPrimaryKey(Integer roleId);

    int insert(RoleBean record);

    int insertSelective(RoleBean record);

    RoleBean selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(RoleBean record);

    int updateRole(RoleBean record);

    RoleBean selectByRoleId(Integer roleId);

    int getTotal();
}