package com.fytmss.beans.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
@author wgq
@create 2024/6/1-周六 14:50
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelBean {
    private String legalNo;

    private String travelPassword;

    private String travelName;

    private String remarkName;

    /**
    * 0正常1出境2入境
    */
    private String permission;

    private String legalName;

    private String legalPhone;

    /**
    * 旅行社余额
    */
    private Double money;

    /**
    * 1正常、0冻结
    */
    private String legalState;

    private Integer isDel;

}