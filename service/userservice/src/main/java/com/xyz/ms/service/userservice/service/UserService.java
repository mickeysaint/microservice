package com.xyz.ms.service.userservice.service;

import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends BaseService<UserPo> {

    @Autowired
    @Override
    @Qualifier("userDao")
    public void setDao(BaseDao<UserPo> dao) {
        this.dao = dao;
    }

    public boolean exists(String username) {
        UserPo userPo = findByUsername(username);
        return (userPo!=null);
    }

    public UserPo findByUsername(String username) {
        UserPo eg = new UserPo();
        eg.setUsername(username);
        List<UserPo> userList = findByEg(eg);
        if (userList != null && userList.size() > 0) {
            return userList.get(0);
        }

        return null;
    }

}
