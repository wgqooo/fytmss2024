package com.fytmss.mapper.base;

import com.fytmss.beans.base.ShipBean;
import com.fytmss.beans.base.UserBean;
import com.fytmss.beans.voyage.VoyageBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
@author wgq
@create 2024/5/30-周四 23:12
*/
@Mapper
public interface ShipBeanMapper {

    List<ShipBean> queryShipsInfo();

    int deleteByPrimaryKey(String shipno);

    int insert(ShipBean record);

    int insertSelective(ShipBean record);

    ShipBean selectByPrimaryKey(String shipno);

    int updateByPrimaryKeySelective(ShipBean record);

    int updateByPrimaryKey(ShipBean record);

    List<ShipBean> queryAllShips();
}