package com.xyz.ms.service.userservice.dao;

import com.xyz.base.po.user.RolePo;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository("roleDao")
public class RoleDao extends BaseDao<RolePo> {

    public List<RolePo> getRoleListByUser(UserPo userPo) {
        String sql = "SELECT A.ID, A.ROLE_CODE, A.ROLE_NAME, A.ORG_ID FROM TU_ROLE A " +
                "JOIN TU_ROLE_USER B ON A.ID=B.ROLE_ID " +
                "WHERE B.USER_ID=? ";

        return this.findBySql(sql, Arrays.asList(userPo.getId()));
    }

}
