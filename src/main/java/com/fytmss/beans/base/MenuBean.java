package com.fytmss.beans.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
@author wgq
@create 2024/5/20-周一 21:04
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer menuId;

    private String menuName;

    private String url;

    private String path;

    private Integer parentId;

    private Byte enabled;

    private String icon;


}