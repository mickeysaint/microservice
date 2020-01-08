package com.xyz.ms.service.userservice.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xyz.base.common.Page;
import com.xyz.base.po.user.OrgPo;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.vo.user.UserVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("userDao")
public class UserDao extends BaseDao<UserPo> {

    @Autowired
    private OrgDao orgDao;

    public Page<UserVo> getListData(Long pageIndex, Long pageSize,
                                    List<OrgPo> orgList, String username, String userFullName) {
        String sql = "select a.ID, A.USER_NAME, A.USER_FULL_NAME, GROUP_CONCAT(c.ID SEPARATOR ',') ORG_IDS, " +
                "GROUP_CONCAT(c.ORG_NAME SEPARATOR ',') ORG_NAMES, " +
                "GROUP_CONCAT(e.ID SEPARATOR ',') ROLE_IDS, " +
                "GROUP_CONCAT(e.ROLE_NAME SEPARATOR ',') ROLE_NAMES " +
                " from TU_USER a " +
                "join tu_org_user b on a.id=b.USER_ID " +
                "join tu_org c on b.org_id=c.id " +
                "left join tu_role_user d on a.id=d.user_id " +
                "left join tu_role e on d.role_id=e.id " +
                "where 1=1 %s " +
                "GROUP BY a.ID, A.USER_NAME, A.USER_FULL_NAME ";
        String dsql = "";
        List params = new ArrayList();
        List<Long> orgIdList = orgDao.getOrgIdList(orgList);
        if (orgIdList != null && orgIdList.size() > 0) {
            dsql += " and c.ID in (" + StringUtils.join(orgIdList, ",") + ") ";
        }

        if (StringUtils.isNotEmpty(username)) {
            dsql += " and a.USER_NAME like ? ";
            params.add("%" + username + "%");
        }

        if (StringUtils.isNotEmpty(userFullName)) {
            dsql += " and a.USER_FULL_NAME like ? ";
            params.add("%" + userFullName + "%");
        }

        Page<UserVo> page = this.findPageBySql(String.format(sql, dsql), params, pageIndex.intValue(), pageSize.intValue(), UserVo.class);
        return page;
    }

    public void deleteOrgUser(Long userId) {
        this.jt.update("delete from tu_org_user where USER_ID=?", userId);
    }

    public void deleteRoleUser(Long userId) {
        this.jt.update("delete from tu_role_user where USER_ID=?", userId);
    }

    public void addOrgUser(Long userId, String orgIds) {
        JSONArray jaOrg = JSON.parseArray(orgIds);
        if (jaOrg.size() == 0) {
            return;
        }
        Long orgId = jaOrg.getLong(jaOrg.size()-1);
        this.jt.update(
                "insert into tu_org_user(ORG_ID, USER_ID) values(?, ?)",
                orgId, userId
        );
    }

    public void addRoleUser(Long userId, String roleIds) {
        JSONArray jaRole = JSON.parseArray(roleIds);
        if (jaRole.size() == 0) {
            return;
        }
        for (int i=0; i<jaRole.size(); i++) {
            this.jt.update(
                    "insert into tu_role_user(ROLE_ID, USER_ID) values(?, ?)",
                    jaRole.getLong(i), userId
            );
        }
    }
}
