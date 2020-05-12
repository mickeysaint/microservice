package com.xyz.ms.service.baseservice.service;

import com.xyz.base.common.Constants;
import com.xyz.base.common.Page;
import com.xyz.base.po.base.SysConfigPo;
import com.xyz.base.service.BaseService;
import com.xyz.base.util.StringUtil;
import com.xyz.ms.service.baseservice.dao.SysConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysConfigService extends BaseService<SysConfigPo> {

    @Autowired
    private SysConfigDao sysConfigDao;

    @Autowired
    public void setDao(SysConfigDao dao) {
        this.dao = dao;
    }

    public boolean exists(String configKey) {
        SysConfigPo sysConfigPo = findByConfigKey(configKey);
        return (sysConfigPo!=null);
    }

    public SysConfigPo findByConfigKey(String configKey) {
        SysConfigPo eg = new SysConfigPo();
        eg.setConfigKey(configKey);
        List<SysConfigPo> sysConfigPoList = findByEg(eg);
        if (sysConfigPoList != null && sysConfigPoList.size() > 0) {
            return sysConfigPoList.get(0);
        }

        return null;
    }

    public Page<SysConfigPo> getListData(Map params) {
        String configKey = StringUtil.objToString(params.get("configKey"));
        String configName = StringUtil.objToString(params.get("configName"));
        Long pageIndex = StringUtil.objToLong(params.get("pageIndex"));
        pageIndex = pageIndex==null?1:pageIndex;
        Long pageSize = StringUtil.objToLong(params.get("pageSize"));
        pageSize = pageSize==null? Constants.PAGE_SIZE_DEFAULT :pageSize;

        return sysConfigDao.getListData(pageIndex, pageSize, configKey, configName);
    }
}
