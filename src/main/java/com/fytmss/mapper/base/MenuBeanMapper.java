package com.fytmss.mapper.base;

import com.fytmss.beans.base.MenuBean;
import org.apache.ibatis.annotations.Mapper;

/**
@author wgq
@create 2024/5/20-周一 21:04
*/
@Mapper
public interface MenuBeanMapper {
    int deleteByPrimaryKey(Integer menuId);

    int insert(MenuBean record);

    int insertSelective(MenuBean record);

    MenuBean selectByPrimaryKey(Integer menuId);

    int updateByPrimaryKeySelective(MenuBean record);

    int updateByPrimaryKey(MenuBean record);

    MenuBean queryMenuByPath(String path);
}