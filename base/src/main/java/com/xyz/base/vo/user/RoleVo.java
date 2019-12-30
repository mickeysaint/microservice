package com.xyz.base.vo.user;

import com.xyz.base.po.user.RolePo;

import javax.persistence.Column;

public class RoleVo extends RolePo {

    @Column(name="ORG_NAME")
    private String orgName;

    @Column(name="ORG_FULL_NAME")
    private String orgFullName;

    @Column(name="ORG_ID_FULL")
    private String orgIdFull; // 一维向量

    @Column(name="MENU_ID_FULLS")
    private String menuIdFulls; // 二维向量

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgFullName() {
        return orgFullName;
    }

    public void setOrgFullName(String orgFullName) {
        this.orgFullName = orgFullName;
    }

    public String getOrgIdFull() {
        return orgIdFull;
    }

    public void setOrgIdFull(String orgIdFull) {
        this.orgIdFull = orgIdFull;
    }

    public String getMenuIdFulls() {
        return menuIdFulls;
    }

    public void setMenuIdFulls(String menuIdFulls) {
        this.menuIdFulls = menuIdFulls;
    }
}
