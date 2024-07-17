package com.fytmss.beans.ticket;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
@author wgq
@create 2024/6/22-周六 21:58
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String tickNo;

    private String passportNo;

    private String trName;

    private String legalNo;

    private String bookPassNo;

    private String teamNo;

    private String tickDate;

    private String startDate;

    private String startVoyNo;

    private String startSeat;

    private String returnDate;

    private String returnVoyNo;

    private String returnSeat;

    private String printDate;

    private String state;

    private String stateDate;

    private String changeTDate;

    private Double tickRefund;

    private Double tickCost;

    private String payType;

    private String isGuide;

    private Integer guideNum;

    /**
    * 0是旅行社，1是售票窗口，2是微信小程序
    */
    private Integer tickOrigin;

    private String tickType;

    private String travellerType;

    private String travelType;

    private Integer startSeatNo;

    private Integer returnSeatNo;

    private String returnState;

    private String orderNo;

    private String returnStateDate;

    private String isNoticed;

    private String orderName;

    private String returnName;

    private Integer isPrint;

    private Integer invoiceIsPrint;

    private String newOrderNo;

    private Double oldTickCost;

    private Double ticketUpdateFee;

    private String shipName;

    private String returnShipName;

    private String travelName;
}