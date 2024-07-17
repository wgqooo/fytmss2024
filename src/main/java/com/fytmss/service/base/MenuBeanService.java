package com.fytmss.service.base;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fytmss.beans.base.MenuBean;
import com.fytmss.mapper.base.MenuBeanMapper;
/**
@author wgq
@create 2024/5/20-周一 21:04
*/
@Service
public class MenuBeanService{

    @Resource
    private MenuBeanMapper menuBeanMapper;

    
    public int deleteByPrimaryKey(Integer menuid) {
        return menuBeanMapper.deleteByPrimaryKey(menuid);
    }

    
    public int insert(MenuBean record) {
        return menuBeanMapper.insert(record);
    }

    
    public int insertSelective(MenuBean record) {
        return menuBeanMapper.insertSelective(record);
    }

    
    public MenuBean selectByPrimaryKey(Integer menuid) {
        return menuBeanMapper.selectByPrimaryKey(menuid);
    }

    
    public int updateByPrimaryKeySelective(MenuBean record) {
        return menuBeanMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(MenuBean record) {
        return menuBeanMapper.updateByPrimaryKey(record);
    }

}
