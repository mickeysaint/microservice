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

    @Column(name="ORG_CODE")
    private String orgCode;

    @Column(name="ORG_NAME")
    private String orgName;

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
}
