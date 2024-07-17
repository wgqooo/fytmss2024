package com.fytmss.service.base;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.fytmss.mapper.base.RoleBeanMapper;
import com.fytmss.beans.base.RoleBean;

import java.util.List;

/**
@author wgq
@create 2024/5/20-周一 20:34
*/
@Service
public class RoleBeanService{

    @Autowired
    private RoleBeanMapper roleBeanMapper;

    public List<RoleBean> getAllRoles(){
        return roleBeanMapper.queryAllRoles();
    }
    
    public int deleteByPrimaryKey(Integer roleid) {
        return roleBeanMapper.deleteByPrimaryKey(roleid);
    }

    
    public int insert(RoleBean record) {
        return roleBeanMapper.insert(record);
    }

    
    public int insertSelective(RoleBean record) {
        return roleBeanMapper.insertSelective(record);
    }

    
    public RoleBean selectByPrimaryKey(Integer roleid) {
        return roleBeanMapper.selectByPrimaryKey(roleid);
    }

    
    public int updateByPrimaryKeySelective(RoleBean record) {
        return roleBeanMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(RoleBean record) {
        return roleBeanMapper.updateByPrimaryKey(record);
    }

}
