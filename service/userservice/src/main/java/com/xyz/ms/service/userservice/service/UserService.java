package com.xyz.ms.service.userservice.service;

import com.xyz.base.dbutil.BaseJdbcService;
import com.xyz.base.dbutil.TuserUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends BaseJdbcService<TuserUser> {

    @Autowired
    public UserService(JdbcTemplate jt) {
        super(jt);
    }

    public boolean exists(String userCode) {
        TuserUser tuserUser = findByCode(userCode);
        return (tuserUser!=null);
    }

    public TuserUser findByCode(String userCode) {
        TuserUser eg = new TuserUser();
        eg.setUserCode(userCode);
        List<TuserUser> tuserUserList = findByEg(eg);
        if (tuserUserList != null && tuserUserList.size() > 0) {
            return tuserUserList.get(0);
        }

        return null;
    }
}
