package com.fytmss.service.base;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import com.fytmss.mapper.base.UserRoleBeanMapper;
import com.fytmss.beans.base.UserRoleBean;

import java.util.List;

/**
@author wgq
@create 2024/5/20-周一 20:50
*/
@Service
public class UserRoleBeanService {

    @Resource
    private UserRoleBeanMapper userRoleBeanMapper;

    public int deleteByEmpNo(String empNo){
        return userRoleBeanMapper.deleteByEmpNo(empNo);
    }


    public UserRoleBean getUserRoleBean(String empNo){
        return userRoleBeanMapper.queryByEmpNo(empNo);
    }

    /**
     *
     * @param empNo 根据empNo获取用户的角色列表
     * @return
     */
    public List<Integer> getRoleIdList(Integer empNo){
        return userRoleBeanMapper.queryRoleIdList(empNo);
    }


    
    public int deleteByPrimaryKey(Integer emproleid) {
        return userRoleBeanMapper.deleteByPrimaryKey(emproleid);
    }

    
    public int insert(UserRoleBean record) {
        return userRoleBeanMapper.insert(record);
    }

    
    public int insertSelective(UserRoleBean record) {
        return userRoleBeanMapper.insertSelective(record);
    }

    
    public UserRoleBean selectByPrimaryKey(Integer emproleid) {
        return userRoleBeanMapper.selectByPrimaryKey(emproleid);
    }

    
    public int updateByPrimaryKeySelective(UserRoleBean record) {
        return userRoleBeanMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(UserRoleBean record) {
        return userRoleBeanMapper.updateByPrimaryKey(record);
    }

}
