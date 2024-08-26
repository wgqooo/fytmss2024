package com.fytmss.mapper.base;

import com.fytmss.beans.base.SysparaBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
@author wgq
@create 2024/7/16-周二 14:23
*/
@Mapper
public interface SysparaBeanMapper {

    int insert(SysparaBean record);

    int insertSelective(SysparaBean record);

    List<SysparaBean> queryParamsInfo();

    int update(SysparaBean sysparaBean);

    int delete(@Param("dType") String dType, @Param("dCode") Integer dCode);

    List<String> queryAllTypes();
}