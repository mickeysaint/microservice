package com.xyz.base.vo.user;

import com.alibaba.fastjson.JSONArray;
import com.xyz.base.po.user.RolePo;

import javax.persistence.Column;

public class RoleVo extends RolePo {

    @Column(name="ORG_NAME")
    private String orgName;

    @Column(name="ORG_FULL_NAME")
    private String orgFullName;

    private JSONArray orgIdFull; // 一维向量

    private JSONArray menuIdFulls; // 二维向量

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

    public JSONArray getOrgIdFull() {
        return orgIdFull;
    }

    public void setOrgIdFull(JSONArray orgIdFull) {
        this.orgIdFull = orgIdFull;
    }

    public JSONArray getMenuIdFulls() {
        return menuIdFulls;
    }

    public void setMenuIdFulls(JSONArray menuIdFulls) {
        this.menuIdFulls = menuIdFulls;
    }
}
