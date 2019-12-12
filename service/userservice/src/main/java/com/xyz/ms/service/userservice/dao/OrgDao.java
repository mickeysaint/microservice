package com.xyz.ms.service.userservice.dao;

import com.xyz.base.po.user.OrgPo;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.util.TreeUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository("orgDao")
public class OrgDao extends BaseDao<OrgPo> {

    public List<OrgPo> getOrgListByUser(UserPo userPo) {
        String sql = "SELECT A.ID, A.ORG_CODE, A.ORG_NAME, A.PARENT_ID FROM TU_ORG A " +
                "JOIN TU_ORG_USER B ON A.ID=B.ORG_ID " +
                "WHERE B.USER_ID=? ";

        return this.findBySql(sql, Arrays.asList(userPo.getId()));
    }

    public OrgPo findTreeById(Long id) {
        OrgPo orgPo = findById(id);
        String sql = "SELECT A.ID, A.ORG_CODE, A.ORG_NAME, A.PARENT_ID FROM TU_ORG A " +
                "WHERE A.ORG_CODE LIKE ? ORDER BY A.ORG_CODE ASC ";
        List<OrgPo> list = this.findBySql(sql, Arrays.asList(id));
        List<OrgPo> chilren = TreeUtils.convertList2Tree(list, id);
        orgPo.setChildren(chilren);
        return orgPo;
    }
}
