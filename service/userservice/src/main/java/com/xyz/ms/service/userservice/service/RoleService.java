package com.xyz.ms.service.userservice.service;

import com.xyz.base.po.user.MenuPo;
import com.xyz.base.po.user.RolePo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.service.BaseService;
import com.xyz.ms.service.userservice.dao.MenuDao;
import com.xyz.ms.service.userservice.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleService extends BaseService<RolePo> {

    @Autowired
    @Qualifier("roleDao")
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    @Override
    @Qualifier("roleDao")
    public void setDao(BaseDao<RolePo> dao) {
        this.dao = dao;
    }

    @Transactional
    public void addRole(RolePo rolePo) {
        // 添加角色数据
        this.save(rolePo);

        // 添加角色对应的菜单数据
        List<MenuPo> menuList = rolePo.getMenuList();
        if (menuList != null && menuList.size() > 0) {
            for (MenuPo menuPo : menuList) {
                roleDao.addRoleMenuRef(rolePo.getId(), menuPo.getId());
            }
        }
    }

    @Transactional
    public void updateRole(RolePo rolePo) {
        // 更新角色数据
        this.update(rolePo);

        // 更新角色对应的菜单数据
        roleDao.deleteRoleMenuRef(rolePo.getId());
        List<MenuPo> menuList = rolePo.getMenuList();
        if (menuList != null && menuList.size() > 0) {
            for (MenuPo menuPo : menuList) {
                roleDao.addRoleMenuRef(rolePo.getId(), menuPo.getId());
            }
        }
    }

    @Transactional
    public void deleteRoleByIds(List<Long> roleIdList) {
        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }

        for (Long roleId : roleIdList) {
            roleDao.deleteRoleMenuRef(roleId);
            roleDao.deleteRoleUserRef(roleId);
            roleDao.deleteByIds(Arrays.asList(roleId));
        }
    }

    public RolePo findRoleById(Long id) {
        RolePo rolePo = findById(id);
        List<MenuPo> menuList = menuDao.getMenuListByRole(Arrays.asList(rolePo));
        rolePo.setMenuList(menuList);
        return rolePo;
    }
}
