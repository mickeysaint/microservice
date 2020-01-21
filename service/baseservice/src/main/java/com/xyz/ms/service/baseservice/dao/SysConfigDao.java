package com.xyz.ms.service.baseservice.dao;

import com.xyz.base.common.Page;
import com.xyz.base.po.base.SysConfigPo;
import com.xyz.base.service.BaseDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("sysConfigDao")
public class SysConfigDao extends BaseDao<SysConfigPo> {

    public Page<SysConfigPo> getListData(Long pageIndex, Long pageSize, String configKey, String configName) {
        String sql = "select ID, CONFIG_KEY, CONFIG_NAME, CONFIG_VALUE, REMARK from tb_sys_config where 1=1 %s order by config_key ";
        String dsql = "";
        List params = new ArrayList();

        if (StringUtils.isNotEmpty(configKey)) {
            dsql += " and CONFIG_KEY like ? ";
            params.add("%" + configKey + "%");
        }

        if (StringUtils.isNotEmpty(configName)) {
            dsql += " and CONFIG_NAME like ? ";
            params.add("%" + configName + "%");
        }

        Page<SysConfigPo> page = this.findPageBySql(String.format(sql, dsql), params, pageIndex.intValue(), pageSize.intValue(), SysConfigPo.class);
        return page;
    }

}
