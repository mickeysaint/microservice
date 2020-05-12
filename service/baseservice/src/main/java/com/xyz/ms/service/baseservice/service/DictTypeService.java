package com.xyz.ms.service.baseservice.service;

import com.xyz.base.common.Constants;
import com.xyz.base.common.Page;
import com.xyz.base.po.base.DictTypePo;
import com.xyz.base.service.BaseService;
import com.xyz.base.util.StringUtil;
import com.xyz.ms.service.baseservice.dao.DictTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DictTypeService extends BaseService<DictTypePo> {

    @Autowired
    private DictTypeDao dictTypeDao;

    @Autowired
    public void setDao(DictTypeDao dao) {
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

    public Page<DictTypePo> getListData(Map params) {
        String typeCode = StringUtil.objToString(params.get("typeCode"));
        String typeName = StringUtil.objToString(params.get("typeName"));
        Long pageIndex = StringUtil.objToLong(params.get("pageIndex"));
        pageIndex = pageIndex==null?1:pageIndex;
        Long pageSize = StringUtil.objToLong(params.get("pageSize"));
        pageSize = pageSize==null? Constants.PAGE_SIZE_DEFAULT :pageSize;

        return dictTypeDao.getListData(pageIndex, pageSize, typeCode, typeName);
    }
}
