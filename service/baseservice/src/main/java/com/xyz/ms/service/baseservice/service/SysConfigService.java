package com.xyz.ms.service.baseservice.service;

import com.xyz.base.po.base.SysConfigPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysConfigService extends BaseService<SysConfigPo> {

    @Autowired
    @Qualifier("sysConfigDao")
    @Override
    public void setDao(BaseDao<SysConfigPo> dao) {
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


}
