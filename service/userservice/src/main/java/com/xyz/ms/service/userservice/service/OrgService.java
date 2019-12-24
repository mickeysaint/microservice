package com.xyz.ms.service.userservice.service;

import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.user.OrgPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.service.BaseService;
import com.xyz.ms.service.userservice.dao.OrgDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgService extends BaseService<OrgPo> {

    @Autowired
    @Override
    @Qualifier("orgDao")
    public void setDao(BaseDao<OrgPo> dao) {
        this.dao = dao;
    }

    public OrgPo findTreeById(Long id) {
        OrgDao orgDao = (OrgDao)this.dao;
        return orgDao.findTreeById(id);
    }

    public String createOrgCode(OrgPo orgPo) {
        if (orgPo.getParentId() == null) {
            return "";
        }

        OrgPo parentOrgPo = findById(orgPo.getParentId());
        String parentOrgCode = parentOrgPo.getOrgCode();
        parentOrgCode = StringUtils.isEmpty(parentOrgCode)?"":parentOrgCode;
        OrgPo eg = new OrgPo();
        eg.setParentId(orgPo.getParentId());
        List<OrgPo> children = findByEg(eg, " order by ORG_CODE ");
        int currIndex = 1;
        if (children != null && children.size() > 0) {
            for (OrgPo c : children) {
                String cOrgCode = c.getOrgCode();
                String index = cOrgCode.substring(parentOrgCode.length());
                if (currIndex != Integer.valueOf(index)) {
                    break;
                }
                currIndex++;
            }
        }
        if (currIndex >= 100) {
            throw new BusinessException("达到最大子节点数量");
        }
        return parentOrgCode + StringUtils.leftPad(Integer.toString(currIndex), 2,'0');
    }
}
