package com.xyz.base.po.user;

import com.xyz.base.common.TreeNode;
import com.xyz.base.po.BasePo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

@Table(name="tu_menu")
public class MenuPo extends BasePo implements TreeNode<MenuPo> {

    @Column(name="ID")
    private Long id;

    @Column(name="PARENT_ID")
    private Long parentId;

    @Column(name="MENU_CODE")
    private String menuCode;

    @Column(name="MENU_NAME")
    private String menuName;

    @Column(name="MENU_SHOW_NAME")
    private String menuShowName;

    @Column(name="MENU_COMPONENT")
    private String menuComponent;

    private List<MenuPo> children = null;

    @Override
    public Long getId() {
        return this.id;
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

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuShowName() {
        return menuShowName;
    }

    public void setMenuShowName(String menuShowName) {
        this.menuShowName = menuShowName;
    }

    public String getMenuComponent() {
        return menuComponent;
    }

    public void setMenuComponent(String menuComponent) {
        this.menuComponent = menuComponent;
    }

    @Override
    public List<MenuPo> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<MenuPo> children) {
        this.children = children;
    }
}
