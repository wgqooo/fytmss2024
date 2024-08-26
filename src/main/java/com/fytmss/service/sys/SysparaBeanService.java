package com.fytmss.service.sys;

import com.fytmss.beans.base.SysparaBean;
import com.fytmss.mapper.base.SysparaBeanMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wgq
 * @create 2024/7/30-周二 21:55
 */
@Service
public class SysparaBeanService {


    @Resource
    private SysparaBeanMapper sysparaBeanMapper;

    public int insert(SysparaBean record) {
        return sysparaBeanMapper.insert(record);
    }


    public int insertSelective(SysparaBean record) {
        return sysparaBeanMapper.insertSelective(record);
    }

    public PageInfo<SysparaBean> findAllParamsByPage(int index, int size) {
        PageHelper.startPage(index, size);
        List<SysparaBean> sysparams = sysparaBeanMapper.queryParamsInfo();
        return new PageInfo<>(sysparams);
    }

    public int update(SysparaBean sysparaBean) {
        return sysparaBeanMapper.update(sysparaBean);
    }

    public int delete(String dType, Integer dCode) {
        return sysparaBeanMapper.delete(dType, dCode);
    }

    public List<String> findAllTypes() {
        return sysparaBeanMapper.queryAllTypes();
    }
}
