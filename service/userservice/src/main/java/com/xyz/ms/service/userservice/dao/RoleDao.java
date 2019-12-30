package com.xyz.ms.service.userservice.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xyz.base.common.Page;
import com.xyz.base.po.user.OrgPo;
import com.xyz.base.po.user.RolePo;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.util.MapUtils;
import com.xyz.base.vo.user.RoleVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public Page<RoleVo> getListData(Long pageIndex, Long pageSize, List<OrgPo> orgList, String roleCode, String roleName) {

        String sql = "select a.ID, a.ORG_ID, A.ROLE_CODE, A.ROLE_NAME, b.ORG_NAME, b.ID_FULL ORG_ID_FULL, " +
                "concat('[', GROUP_CONCAT(d.ID_FULL SEPARATOR ','), ']') MENU_ID_FULLS  from TU_ROLE a " +
                "join tu_org b on a.org_id=b.id " +
                "join tu_role_menu c on a.id=c.role_id " +
                "join tu_menu d on c.menu_id=d.id " +
                "where 1=1 %s " +
                "GROUP BY a.ID, a.ORG_ID, A.ROLE_CODE, A.ROLE_NAME, b.ORG_NAME, b.ID_FULL ";
        String dsql = "";
        List params = new ArrayList();
        List<Long> orgIdList = getOrgIdList(orgList);
        if (orgIdList != null && orgIdList.size() > 0) {
            dsql += " and (a.org_id in (" + StringUtils.join(orgIdList, ",") + ") or a.org_id is null) ";
        } else {
            dsql += " and (a.org_id is null) ";
        }

        if (StringUtils.isNotEmpty(roleCode)) {
            dsql += " and a.role_code like ? ";
            params.add("%" + roleCode + "%");
        }

        if (StringUtils.isNotEmpty(roleName)) {
            dsql += " and a.role_name like ? ";
            params.add("%" + roleName + "%");
        }

        Page<RoleVo> page = this.findPageBySql(String.format(sql, dsql), params, pageIndex.intValue(), pageSize.intValue(), RoleVo.class);
        List<RoleVo> roleVoList = page.getDataList();
        justfyOrgIdFull(roleVoList, orgList); // 因为页面展示的orgId向量并非从root开始，所以从orgList开始进行修正
        return page;
    }

    /**
     * 因为页面展示的orgId向量并非从root开始，所以从orgList开始进行修正
     * @param roleVoList
     * @param orgList
     */
    private void justfyOrgIdFull(List<RoleVo> roleVoList, List<OrgPo> orgList) {
        if (roleVoList == null || roleVoList.size() == 0) {
            return;
        }

        Map<Long, OrgPo> orgPoMapping = MapUtils.convertList2Map(orgList, "id", Long.class, OrgPo.class);
        for (RoleVo roleVo : roleVoList) {
            String orgIdFull = roleVo.getOrgIdFull();
            String orgIdFullJustified = getOrgIdFullJustified(orgIdFull, orgList);
            roleVo.setOrgIdFull(orgIdFullJustified);
        }
    }

    private String getOrgIdFullJustified(String orgIdFull, List<OrgPo> orgList) {
        if (StringUtils.isEmpty(orgIdFull)) {
            return "[]";
        }

        String ret = "[]";
        JSONArray jaOrgIdFull = JSON.parseArray(orgIdFull);
        if (jaOrgIdFull.size() == 0) {
            return ret;
        }

        for (OrgPo org: orgList) {
            String idFull = org.getIdFull();
            JSONArray ja = JSON.parseArray(idFull);
            Long orgId = org.getId();

            JSONArray jaNew = new JSONArray();
            boolean matched = false;
            for (int i=0; i<jaOrgIdFull.size(); i++) {
                if (matched) {
                    jaNew.add(jaOrgIdFull.getLong(i));
                } else {
                    if (orgId.equals(jaOrgIdFull.getLong(i))) {
                        matched = true;
                        jaNew.add(jaOrgIdFull.getLong(i));
                    }
                }
            }

            if (jaNew.size() > 0) {
                ret = jaNew.toJSONString();
                break;
            }
        }

        return ret;
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
