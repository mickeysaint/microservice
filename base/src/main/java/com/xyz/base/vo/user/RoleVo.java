package com.xyz.base.vo.user;

import com.alibaba.fastjson.JSONArray;
import com.xyz.base.po.user.RolePo;

import javax.persistence.Column;

public class RoleVo extends RolePo {

    @Column(name="ORG_NAME")
    private String orgName;

    @Column(name="ORG_FULL_NAME")
    private String orgFullName;

    private JSONArray orgIdForPage; // 一维向量

    private JSONArray menuIdsForPage; // 二维向量

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

    public JSONArray getOrgIdForPage() {
        return orgIdForPage;
    }

    public void setOrgIdForPage(JSONArray orgIdForPage) {
        this.orgIdForPage = orgIdForPage;
    }

    public JSONArray getMenuIdsForPage() {
        return menuIdsForPage;
    }

    public void setMenuIdsForPage(JSONArray menuIdsForPage) {
        this.menuIdsForPage = menuIdsForPage;
    }
}
