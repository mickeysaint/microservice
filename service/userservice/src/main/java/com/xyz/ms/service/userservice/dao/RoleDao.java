package com.xyz.ms.service.userservice.dao;

import com.xyz.base.po.user.RolePo;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void addRoleMenuRef(Long roleId, Long menuId) {
        String sql = "insert into tu_role_menu(ROLE_ID, MENU_ID) values(?, ?)";
        this.jt.update(sql, roleId, menuId);
    }

    @Transactional
    public void deleteRoleMenuRef(Long roleId) {
        String sql = "delete from tu_role_menu where ROLE_ID=? ";
        this.jt.update(sql, roleId);
    }

    @Transactional
    public void deleteRoleUserRef(Long roleId) {
        String sql = "delete from tu_role_user where ROLE_ID=? ";
        this.jt.update(sql, roleId);
    }
}
