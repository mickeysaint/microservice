package com.xyz.ms.service.baseservice.service;

import com.xyz.base.dbutil.BaseJdbcService;
import com.xyz.ms.service.baseservice.bean.TbaseDictType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TbaseDictTypeService extends BaseJdbcService<TbaseDictType> {

    @Autowired
    public TbaseDictTypeService(JdbcTemplate jt) {
        super(jt);
    }

    public boolean exists(String typeCode) {
        TbaseDictType tbaseDictType = findByCode(typeCode);
        return (tbaseDictType!=null);
    }

    public TbaseDictType findByCode(String typeCode) {
        TbaseDictType eg = new TbaseDictType();
        eg.setTypeCode(typeCode);
        List<TbaseDictType> tbaseDictTypeList = findByEg(eg);
        if (tbaseDictTypeList != null && tbaseDictTypeList.size() > 0) {
            return tbaseDictTypeList.get(0);
        }

        return null;
    }
}
