package com.fytmss.beans.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
@author wgq
@create 2024/5/20-周一 20:50
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer empRoleId;

    private String empNo;

    private Integer roleId;


}