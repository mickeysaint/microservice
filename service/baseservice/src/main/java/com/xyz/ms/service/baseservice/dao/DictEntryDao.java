package com.xyz.ms.service.baseservice.dao;

import com.xyz.base.common.Page;
import com.xyz.base.po.base.DictEntryPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.vo.user.DictEntryVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("dictEntryDao")
public class DictEntryDao extends BaseDao<DictEntryPo> {

    public Page<DictEntryVo> getListData(Long pageIndex, Long pageSize,
                                         String typeCode, String entryCode, String entryName) {
        String sql = "select A.ID, A.TYPE_CODE, A.ENTRY_CODE, A.ENTRY_NAME, B.TYPE_NAME from tb_dict_entry a " +
                "join tb_dict_type b on a.TYPE_CODE=b.TYPE_CODE " +
                "where 1=1 %s order by a.TYPE_CODE, a.ENTRY_CODE ";
        String dsql = "";
        List params = new ArrayList();

        if (StringUtils.isNotEmpty(typeCode)) {
            dsql += " and TYPE_CODE like ? ";
            params.add("%" + typeCode + "%");
        }

        if (StringUtils.isNotEmpty(entryCode)) {
            dsql += " and ENTRY_CODE like ? ";
            params.add("%" + entryCode + "%");
        }

        if (StringUtils.isNotEmpty(entryName)) {
            dsql += " and ENTRY_NAME like ? ";
            params.add("%" + entryName + "%");
        }

        Page<DictEntryVo> page = this.findPageBySql(String.format(sql, dsql), params, pageIndex.intValue(), pageSize.intValue(), DictEntryVo.class);
        return page;
    }

}
