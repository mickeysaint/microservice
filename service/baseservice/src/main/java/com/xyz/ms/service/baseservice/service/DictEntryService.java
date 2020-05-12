package com.xyz.ms.service.baseservice.service;

import com.xyz.base.common.Constants;
import com.xyz.base.common.Page;
import com.xyz.base.po.base.DictEntryPo;
import com.xyz.base.service.BaseService;
import com.xyz.base.util.StringUtil;
import com.xyz.base.vo.user.DictEntryVo;
import com.xyz.ms.service.baseservice.dao.DictEntryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DictEntryService extends BaseService<DictEntryPo> {

    @Autowired
    private DictEntryDao dictEntryDao;

    @Autowired
    public void setDao(DictEntryDao dao) {
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

    public Page<DictEntryVo> getListData(Map params) {
        String typeCode = StringUtil.objToString(params.get("typeCode"));
        String entryCode = StringUtil.objToString(params.get("entryCode"));
        String entryName = StringUtil.objToString(params.get("entryName"));
        Long pageIndex = StringUtil.objToLong(params.get("pageIndex"));
        pageIndex = pageIndex==null?1:pageIndex;
        Long pageSize = StringUtil.objToLong(params.get("pageSize"));
        pageSize = pageSize==null? Constants.PAGE_SIZE_DEFAULT :pageSize;

        return dictEntryDao.getListData(pageIndex, pageSize, typeCode, entryCode, entryName);
    }
}
