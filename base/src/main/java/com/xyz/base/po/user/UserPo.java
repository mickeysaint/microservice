package com.xyz.base.po.user;

import com.xyz.base.po.BasePo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

@Table(name="tu_user")
public class UserPo extends BasePo {

    @Column(name="ID")
    private Long id;

    @Column(name="USER_NAME")
    private String username;

    @Column(name="USER_FULL_NAME")
    private String userFullName;

    @Column(name="PASSWORD")
    private String password;

    private String accessToken;

    private List<RolePo> roleList = null;

    private List<MenuPo> menuList = null;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<RolePo> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RolePo> roleList) {
        this.roleList = roleList;
    }

    public List<MenuPo> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuPo> menuList) {
        this.menuList = menuList;
    }
}
