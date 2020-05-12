package com.xyz.ms.service.baseservice.dao;

import com.xyz.base.common.Page;
import com.xyz.base.po.base.DictTypePo;
import com.xyz.base.po.base.SysConfigPo;
import com.xyz.base.service.BaseDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("dictTypeDao")
public class DictTypeDao extends BaseDao<DictTypePo> {
    public Page<DictTypePo> getListData(Long pageIndex, Long pageSize, String typeCode, String typeName) {
        String sql = "select ID, TYPE_CODE, TYPE_NAME from tb_dict_type where 1=1 %s order by TYPE_CODE ";
        String dsql = "";
        List params = new ArrayList();

        if (StringUtils.isNotEmpty(typeCode)) {
            dsql += " and TYPE_CODE like ? ";
            params.add("%" + typeCode + "%");
        }

        if (StringUtils.isNotEmpty(typeName)) {
            dsql += " and TYPE_NAME like ? ";
            params.add("%" + typeName + "%");
        }

        Page<DictTypePo> page = this.findPageBySql(String.format(sql, dsql), params, pageIndex.intValue(), pageSize.intValue(), DictTypePo.class);
        return page;
    }
}
