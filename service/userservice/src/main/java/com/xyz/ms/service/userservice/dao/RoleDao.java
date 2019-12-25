package com.xyz.ms.service.userservice.dao;

import com.xyz.base.common.Page;
import com.xyz.base.po.user.OrgPo;
import com.xyz.base.po.user.RolePo;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository("roleDao")
public class RoleDao extends BaseDao<RolePo> {

    public List<RolePo> getRoleListByUser(UserPo userPo) {
        String sql = "SELECT A.ID, A.ROLE_CODE, A.ROLE_NAME, A.ORG_ID FROM TU_ROLE A " +
                "JOIN TU_ROLE_USER B ON A.ID=B.ROLE_ID " +
                "WHERE B.USER_ID=? ";

        return this.findBySql(sql, Arrays.asList(userPo.getId()));
    }

    @Transactional
    public void addRoleMenuRef(Long roleId, Long menuId) {
        String sql = "insert into tu_role_menu(ROLE_ID, MENU_ID) values(?, ?)";
        this.jt.update(sql, roleId, menuId);
    }

    @Transactional
    public void deleteRoleMenuRef(Long roleId) {
        String sql = "delete from tu_role_menu where ROLE_ID=? ";
        this.jt.update(sql, roleId);
    }

    @Transactional
    public void deleteRoleUserRef(Long roleId) {
        String sql = "delete from tu_role_user where ROLE_ID=? ";
        this.jt.update(sql, roleId);
    }

    public Page<RolePo> getListData(Long pageIndex, Long pageSize, List<OrgPo> orgList, String roleCode, String roleName) {

        String sql = "select * from TU_ROLE where 1=1 %s ";
        String dsql = "";
        List params = new ArrayList();
        List<Long> orgIdList = getOrgIdList(orgList);
        if (orgIdList != null && orgIdList.size() > 0) {
            dsql += " and (org_id in (" + StringUtils.join(orgIdList, ",") + ") or org_id is null) ";
        } else {
            dsql += " and (org_id is null) ";
        }

        if (StringUtils.isNotEmpty(roleCode)) {
            dsql += " and role_code like ? ";
            params.add("%" + roleCode + "%");
        }

        if (StringUtils.isNotEmpty(roleName)) {
            dsql += " and role_name like ? ";
            params.add("%" + roleName + "%");
        }

        Page<RolePo> page = this.findPageBySql(String.format(sql, dsql), params, pageIndex.intValue(), pageSize.intValue());
        return page;
    }

    private List<Long> getOrgIdList(List<OrgPo> orgList) {
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
