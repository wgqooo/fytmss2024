package com.fytmss.beans.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
@author wgq
@create 2024/7/16-周二 14:23
*/

/*
Lombok 在前后端交互时因为JavaBean中get/set导致字段大小写不一致的问题,
一般常见的字段遵循驼峰命名没有任何问题，但一些特殊的字段比如cId传到前端却变成了cid，
导致字段不一致，赋值出现问题，归根结底是get/set出现了问题。
 */
@Builder
public class SysparaBean {

    private String dType;

    private Integer dCode;

    private String cPara;

    private String dValue;

    private String dTime;

    public String getDType() {
        return dType;
    }

    public void setDType(String dType) {
        this.dType = dType;
    }

    public Integer getDCode() {
        return dCode;
    }

    public void setDCode(Integer dCode) {
        this.dCode = dCode;
    }

    public String getCPara() {
        return cPara;
    }

    public void setCPara(String cPara) {
        this.cPara = cPara;
    }

    public String getDValue() {
        return dValue;
    }

    public void setDValue(String dValue) {
        this.dValue = dValue;
    }

    public String getDTime() {
        return dTime;
    }

    public void setDTime(String dTime) {
        this.dTime = dTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysparaBean that = (SysparaBean) o;
        return Objects.equals(dType, that.dType) && Objects.equals(dCode, that.dCode) && Objects.equals(cPara, that.cPara) && Objects.equals(dValue, that.dValue) && Objects.equals(dTime, that.dTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dType, dCode, cPara, dValue, dTime);
    }

    @Override
    public String toString() {
        return "SysparaBean{" +
                "dType='" + dType + '\'' +
                ", dCode=" + dCode +
                ", cPara='" + cPara + '\'' +
                ", dValue='" + dValue + '\'' +
                ", dTime='" + dTime + '\'' +
                '}';
    }
}