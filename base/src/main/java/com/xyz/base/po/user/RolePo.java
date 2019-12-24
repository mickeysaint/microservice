package com.xyz.base.po.user;

import com.xyz.base.po.BasePo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

@Table(name="tu_role")
public class RolePo extends BasePo {

    @Column(name="ID")
    private Long id;

    @Column(name="ORG_ID")
    private Long orgId;

    @Column(name="ROLE_CODE")
    private String roleCode;

    @Column(name="ROLE_NAME")
    private String roleName;

    private List<MenuPo> menuList;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<MenuPo> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuPo> menuList) {
        this.menuList = menuList;
    }
}
