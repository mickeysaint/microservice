package com.xyz.ms.service.baseservice.service;

import com.xyz.base.dbutil.BaseJdbcService;
import com.xyz.ms.service.baseservice.bean.TbaseDictEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TbaseDictEntryService extends BaseJdbcService<TbaseDictEntry> {

    @Autowired
    public TbaseDictEntryService(JdbcTemplate jt) {
        super(jt);
    }

    public boolean exists(String typeCode, String entryCode) {
        TbaseDictEntry tbaseDictEntry = findByCode(typeCode, entryCode);
        return (tbaseDictEntry!=null);
    }

    public TbaseDictEntry findByCode(String typeCode, String entryCode) {
        TbaseDictEntry eg = new TbaseDictEntry();
        eg.setTypeCode(typeCode);
        eg.setEntryCode(entryCode);
        List<TbaseDictEntry> tbaseDictEntryList = findByEg(eg);
        if (tbaseDictEntryList != null && tbaseDictEntryList.size() > 0) {
            return tbaseDictEntryList.get(0);
        }

        return null;
    }
}
