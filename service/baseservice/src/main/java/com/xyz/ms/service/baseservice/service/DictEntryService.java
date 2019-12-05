package com.xyz.ms.service.baseservice.service;

import com.xyz.base.po.base.DictEntryPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictEntryService extends BaseService<DictEntryPo> {

    @Autowired
    @Qualifier("dictEntryDao")
    @Override
    public void setDao(BaseDao<DictEntryPo> dao) {
        this.dao = dao;
    }

    public boolean exists(String typeCode, String entryCode) {
        DictEntryPo dictEntry = findByCode(typeCode, entryCode);
        return (dictEntry!=null);
    }

    public DictEntryPo findByCode(String typeCode, String entryCode) {
        DictEntryPo eg = new DictEntryPo();
        eg.setTypeCode(typeCode);
        eg.setEntryCode(entryCode);
        List<DictEntryPo> dictEntryList = findByEg(eg);
        if (dictEntryList != null && dictEntryList.size() > 0) {
            return dictEntryList.get(0);
        }

        return null;
    }
}
