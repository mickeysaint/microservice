package com.xyz.ms.common.oauth2.service;

import com.xyz.base.dbutil.BaseJdbcService;
import com.xyz.base.dbutil.TuserUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseJdbcService<TuserUser> {

    @Autowired
    public UserService(JdbcTemplate jt) {
        super(jt);
    }
}
