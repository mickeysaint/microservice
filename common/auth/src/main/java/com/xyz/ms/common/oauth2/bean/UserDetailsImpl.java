package com.xyz.ms.common.oauth2.bean;

import com.xyz.base.po.user.UserPo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private UserPo userPo;

    public UserDetailsImpl(UserPo userPo) {
        Assert.notNull(userPo, "用户信息不能为空。");
        this.userPo = userPo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userPo.getPassword();
    }

    @Override
    public String getUsername() {
        return userPo.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserPo getUserPo() {
        return userPo;
    }
}
