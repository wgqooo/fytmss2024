package com.fytmss.beans.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
@author wgq
@create 2024/5/30-周四 23:12
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
    * 船舷号
    */
    private String shipNo;

    /**
    * 船名
    */
    private String shipName;

    /**
    * VIP座位数量
    */
    private Integer vipSeat;

    /**
    * 一等座位数量
    */
    private Integer firSeat;

    /**
    * 二等座位数量
    */
    private Integer secSeat;

    /**
    * 字典表：0在港、1运行、2维修
    */
    private Integer shiptimeState;

    /**
    * 船舶的座位排列种类？
    */
    private String seatDis;

    /**
    * 船舶备忘录
    */
    private Integer shipMemo;


}