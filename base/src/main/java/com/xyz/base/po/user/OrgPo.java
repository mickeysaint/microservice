package com.xyz.base.po.user;

import com.xyz.base.common.TreeNode;
import com.xyz.base.po.BasePo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

@Table(name="tu_org")
public class OrgPo extends BasePo implements TreeNode<OrgPo> {

    @Column(name="ID")
    private Long id;

    @Column(name="PARENT_ID")
    private Long parentId;

    @Column(name="ID_FULL")
    private String idFull;

    @Column(name="ORG_CODE")
    private String orgCode;

    @Column(name="ORG_NAME")
    private String orgName;

    @Column(name="ORG_NAME_FULL")
    private String orgNameFull;

    private List<OrgPo> children = null;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public List<OrgPo> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<OrgPo> children) {
        this.children = children;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getIdFull() {
        return idFull;
    }

    public void setIdFull(String idFull) {
        this.idFull = idFull;
    }

    public String getOrgNameFull() {
        return orgNameFull;
    }

    public void setOrgNameFull(String orgNameFull) {
        this.orgNameFull = orgNameFull;
    }
}
