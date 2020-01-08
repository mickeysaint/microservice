package com.xyz.ms.service.userservice.dao;

import com.xyz.base.po.user.OrgPo;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.util.TreeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository("orgDao")
public class OrgDao extends BaseDao<OrgPo> {

    public List<OrgPo> getOrgListByUser(UserPo userPo) {
        String sql = "SELECT A.* FROM TU_ORG A " +
                "JOIN TU_ORG_USER B ON A.ID=B.ORG_ID " +
                "WHERE B.USER_ID=? ";

        List<OrgPo> orgPos = this.findBySql(sql, Arrays.asList(userPo.getId()));
        List<OrgPo> retList = new ArrayList<>();
        if (orgPos != null && orgPos.size() > 0) {
            for (OrgPo orgPo : orgPos) {
                retList.add(findTreeById(orgPo.getId()));
            }
        }
        return retList;
    }

    public OrgPo findTreeById(Long id) {
        OrgPo orgPo = findById(id);
        String sql = "SELECT A.* FROM TU_ORG A " +
                "WHERE A.ORG_CODE LIKE ? ORDER BY A.ORG_CODE ASC ";
        String parentOrgCode = StringUtils.isEmpty(orgPo.getOrgCode())?"":orgPo.getOrgCode();
        List<OrgPo> list = this.findBySql(sql, Arrays.asList(parentOrgCode + "%"));
        List<OrgPo> children = TreeUtils.convertList2Tree(list, id);
        orgPo.setChildren(children);
        return orgPo;
    }

    public List<Long> getOrgIdList(List<OrgPo> orgList) {
        List<Long> retList = new ArrayList<Long>();
        if (orgList != null && orgList.size() > 0) {
            for (OrgPo orgPo : orgList) {
                retList.add(orgPo.getId());
                List<Long> childrenIdList = getOrgIdList(orgPo.getChildren());
                if (childrenIdList != null && childrenIdList.size() > 0) {
                    retList.addAll(childrenIdList);
                }
            }
        }
        return retList;
    }
}
