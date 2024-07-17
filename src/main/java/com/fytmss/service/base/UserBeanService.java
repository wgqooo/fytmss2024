package com.fytmss.service.base;

import com.fytmss.common.utils.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import com.fytmss.mapper.base.UserBeanMapper;
import com.fytmss.beans.base.UserBean;

import java.util.List;

/**
@author wgq
@create 2024/5/17-周五 14:14
*/
@Service
public class UserBeanService {


    @Resource
    private UserBeanMapper userMapper;

    public List<UserBean> getUsersInfo(){
        return userMapper.queryUsersInfo();
    }

    public PageInfo<UserBean> findAllUserByPageS(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserBean> users = userMapper.queryUsersInfo();
        return new PageInfo<>(users);
    }

    public int isFreezeEmp(Integer isEnabled, String empNo){
        return userMapper.updateEnabled(isEnabled, empNo);
    }

    public int deleteByPrimaryKey(String empno) {
        return userMapper.deleteByPrimaryKey(empno);
    }


    public int insert(UserBean record) {
        return userMapper.insert(record);
    }


    public int insertSelective(UserBean record) {
        return userMapper.insertSelective(record);
    }


    public UserBean selectByPrimaryKey(String empno) {
        return userMapper.selectByPrimaryKey(empno);
    }


    public int updateByPrimaryKeySelective(UserBean record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }


    public int updateByPrimaryKey(UserBean record) {
        return userMapper.updateByPrimaryKey(record);
    }

    public void checkLogin(String username, String password, boolean rememberMe) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        subject.login(token);
    }

    //修改密码
    public void resetPassword(String username, String oldPassword, String newPassword) {

    }
}
