package com.xyz.ms.service.baseservice.service;

import com.xyz.base.po.base.DictTypePo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictTypeService extends BaseService<DictTypePo> {

    @Autowired
    @Qualifier("dictTypeDao")
    @Override
    public void setDao(BaseDao<DictTypePo> dao) {
        this.dao = dao;
    }

    public boolean exists(String typeCode) {
        DictTypePo dictTypePo = findByCode(typeCode);
        return (dictTypePo!=null);
    }

    public DictTypePo findByCode(String typeCode) {
        DictTypePo eg = new DictTypePo();
        eg.setTypeCode(typeCode);
        List<DictTypePo> dictTypePoList = findByEg(eg);
        if (dictTypePoList != null && dictTypePoList.size() > 0) {
            return dictTypePoList.get(0);
        }

        return null;
    }
}
