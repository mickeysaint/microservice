package com.xyz.ms.service.baseservice.service;

import com.xyz.base.dbutil.BaseJdbcService;
import com.xyz.ms.service.baseservice.bean.TbaseSysconfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TbaseSysconfigService extends BaseJdbcService<TbaseSysconfig> {

    @Autowired
    public TbaseSysconfigService(JdbcTemplate jt) {
        super(jt);
    }

    public boolean exists(String configKey) {
        TbaseSysconfig tbaseSysconfig = findByConfigKey(configKey);
        return (tbaseSysconfig!=null);
    }

    public TbaseSysconfig findByConfigKey(String configKey) {
        TbaseSysconfig eg = new TbaseSysconfig();
        eg.setConfigKey(configKey);
        List<TbaseSysconfig> tbaseSysconfigList = findByEg(eg);
        if (tbaseSysconfigList != null && tbaseSysconfigList.size() > 0) {
            return tbaseSysconfigList.get(0);
        }

        return null;
    }
}
