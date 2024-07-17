package com.fytmss.beans.voyage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
@author wgq
@create 2024/6/14-周五 14:45
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoyageBean {
    private String voyDate;

    private String voyNo;

    private String shipNo;

    private String reShipNo;

    private String startTime;

    private Integer startPort;

    private Integer firRes;

    private Integer secRes;

    private Integer vipVisa;

    private Integer vipRes;

    private Integer firVisa;

    private Integer secVisa;

    private Integer vipPrice;

    private Integer firPrice;

    private Integer secPrice;

    /**
    * 是否生效：1表示生效
    */
    private Integer voyState;

    private String voyMemo;

    private String shipName;

    private String reShipName;

    //航次总座
    private Integer vipSeat;

    private Integer firSeat;

    private Integer secSeat;
    //航次余座
    private Integer vipLeft;

    private Integer firLeft;

    private Integer secLeft;

    private String teamNo;

    private String travelName;

    private Integer teamNum;

    private String travellerType;
}