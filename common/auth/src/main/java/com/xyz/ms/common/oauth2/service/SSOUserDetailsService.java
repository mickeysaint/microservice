package com.xyz.ms.common.oauth2.service;

import com.xyz.base.dbutil.TuserUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jinbin
 * @date 2019-05-20 20:28
 */

@Component
public class SSOUserDetailsService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TuserUser eg = new TuserUser();
        eg.setUserCode(username);
        List<TuserUser> list = userService.findByEg(eg);
        if(list == null || list.size() == 0) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return new User(username,
                list.get(0).getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
