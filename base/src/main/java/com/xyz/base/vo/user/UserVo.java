package com.xyz.base.vo.user;

import com.xyz.base.po.user.UserPo;

import javax.persistence.Column;

public class UserVo extends UserPo {

    @Column(name="ORG_IDS")
    private String orgIds;

    @Column(name="ROLE_IDS")
    private String roleIds;

    @Column(name="ORG_NAMES")
    private String orgNames;

    @Column(name="ROLE_NAMES")
    private String roleNames;

    public String getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getOrgNames() {
        return orgNames;
    }

    public void setOrgNames(String orgNames) {
        this.orgNames = orgNames;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }
}
