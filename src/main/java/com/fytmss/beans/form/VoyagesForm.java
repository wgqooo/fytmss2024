package com.fytmss.beans.form;

import com.fytmss.beans.voyage.VoyageBean;
import lombok.Data;

import java.util.List;

/**
 * @author wgq
 * @create 2024/6/20-周四 16:42
 */
@Data
public class VoyagesForm {

    private List<VoyageBean> voyages;
    private String startDate;
    private String endDate;
}
