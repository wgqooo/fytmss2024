package com.fytmss.service.base;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import com.fytmss.beans.base.RoleauthBean;
import com.fytmss.mapper.base.RoleauthBeanMapper;

import java.util.List;

/**
@author wgq
@create 2024/5/20-周一 20:59
*/
@Service
public class RoleauthBeanService{

    @Resource
    private RoleauthBeanMapper roleauthBeanMapper;

    public List<Integer> getRoleauthByRoleId(Integer roleId){
        return roleauthBeanMapper.queryRoleauthByRoleId(roleId);
    }

    
    public int deleteByPrimaryKey(Integer rolemenuid) {
        return roleauthBeanMapper.deleteByPrimaryKey(rolemenuid);
    }

    
    public int insert(RoleauthBean record) {
        return roleauthBeanMapper.insert(record);
    }

    
    public int insertSelective(RoleauthBean record) {
        return roleauthBeanMapper.insertSelective(record);
    }

    
    public RoleauthBean selectByPrimaryKey(Integer rolemenuid) {
        return roleauthBeanMapper.selectByPrimaryKey(rolemenuid);
    }

    
    public int updateByPrimaryKeySelective(RoleauthBean record) {
        return roleauthBeanMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(RoleauthBean record) {
        return roleauthBeanMapper.updateByPrimaryKey(record);
    }

    public List<String> getRoleCNameByMenuId(Integer menuId) {
        return roleauthBeanMapper.queryRoleCNameByMenuId(menuId);
    }

    public int deleteAuth(int roleId, int menuId) {
        return roleauthBeanMapper.deleteAuth(roleId, menuId);
    }
}
