package com.xyz.ms.common.oauth2.service;

import com.xyz.base.common.ResultBean;
import com.xyz.base.po.user.UserPo;
import com.xyz.ms.common.oauth2.bean.UserDetailsImpl;
import com.xyz.ms.common.oauth2.client.UserClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author jinbin
 * @date 2019-05-20 20:28
 */

@Component
public class SSOUserDetailsService implements UserDetailsService {

    @Autowired
    private UserClientService userClientService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ResultBean<UserPo> resultBean = userClientService.findByUsername(username);

        if (!resultBean.isSuccess()) {
            throw new UsernameNotFoundException("找不到该用户。");
        }

        return new UserDetailsImpl(resultBean.getData());
    }
}
