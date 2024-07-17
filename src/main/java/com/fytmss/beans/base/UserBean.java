package com.fytmss.beans.base;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wgq
 * @create 2024/5/17-周五 14:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String empNo;

    private String empPassword;

    private String empName;

    private String empSex;

    private String empBirthday;

    private String empMobile;

    private String empAddress;

    private Integer empDept;

    private String empPicture;

    private Integer enabled;

    private String empSalt;

    private String roleName;

    //private String state;
}