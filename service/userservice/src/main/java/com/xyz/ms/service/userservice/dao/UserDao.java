package com.xyz.ms.service.userservice.dao;

import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDao extends BaseDao<UserPo> {
}
