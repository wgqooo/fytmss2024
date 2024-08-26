package com.fytmss.mapper.base;

import com.fytmss.beans.base.RoleauthBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
@author wgq
@create 2024/5/20-周一 20:59
*/
@Mapper
public interface RoleauthBeanMapper {

    /**
     *
     * @param roleId 根据roleId查询menuId的集合
     * @return
     */
    List<Integer> queryRoleauthByRoleId(Integer roleId);

    int deleteByPrimaryKey(Integer roleMenuId);

    int insert(RoleauthBean record);

    int insertSelective(RoleauthBean record);

    RoleauthBean selectByPrimaryKey(Integer roleMenuId);

    int updateByPrimaryKeySelective(RoleauthBean record);

    int updateByPrimaryKey(RoleauthBean record);

    List<String> queryRoleCNameByMenuId(Integer menuId);

    int deleteAuth(int roleId, int menuId);
}