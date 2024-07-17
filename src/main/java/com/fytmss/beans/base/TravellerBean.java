package com.fytmss.beans.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
@author wgq
@create 2024/6/7-周五 15:13
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravellerBean {
    private String passportNo;

    private String trName;

    private String trPinYin;

    private String trPhone;

    private String trTmpPhone;

    private String trPwd;

    private Integer trSex;

    private String trBirthday;

    private String trBorAdd;

    private String trNation;

    private String trTravel;

    private String inSertDate;

    private String trRelationPer;

    private Integer trAuthen;

    private String trPortPhoto;

    private String trPortPhotoBack;

    private Date trCheckTime;

    private Integer isDel;

    private String travelName;

    //判断是否买过同一航次的票 0->无, 1->有
    private Integer hasTicket = 0;

    private String legalNo;

    private Double tickCost;

    private String tickDate;
}