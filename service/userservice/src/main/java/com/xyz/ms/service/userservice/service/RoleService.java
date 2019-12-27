package com.xyz.ms.service.userservice.service;

import com.alibaba.fastjson.JSONArray;
import com.xyz.base.common.Constants;
import com.xyz.base.common.Page;
import com.xyz.base.po.user.MenuPo;
import com.xyz.base.po.user.OrgPo;
import com.xyz.base.po.user.RolePo;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.service.BaseService;
import com.xyz.base.util.StringUtil;
import com.xyz.base.vo.user.RoleVo;
import com.xyz.ms.service.userservice.dao.MenuDao;
import com.xyz.ms.service.userservice.dao.OrgDao;
import com.xyz.ms.service.userservice.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<RolePo> {

    @Autowired
    @Qualifier("roleDao")
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrgDao orgDao;

    @Autowired
    @Override
    @Qualifier("roleDao")
    public void setDao(BaseDao<RolePo> dao) {
        this.dao = dao;
    }

    @Transactional
    public void addRole(RoleVo roleVo) {
        // 添加角色数据
        JSONArray jaOrgIdFull = roleVo.getOrgIdFull();
        if (jaOrgIdFull != null && jaOrgIdFull.size() > 0) {
            roleVo.setOrgId(Long.valueOf(jaOrgIdFull.get(jaOrgIdFull.size()-1).toString()));
        } else {
            roleVo.setOrgId(null);
        }
        this.save(roleVo);

        // 添加角色对应的菜单数据
        roleVo.setMenuList(menuDao.getMenuListByIdFulls(roleVo.getMenuIdFulls()));
        List<MenuPo> menuList = roleVo.getMenuList();
        if (menuList != null && menuList.size() > 0) {
            for (MenuPo menuPo : menuList) {
                roleDao.addRoleMenuRef(roleVo.getId(), menuPo.getId());
            }
        }
    }

    @Transactional
    public void updateRole(RoleVo roleVo) {
        // 更新角色数据
        JSONArray jaOrgIdFull = roleVo.getOrgIdFull();
        if (jaOrgIdFull != null && jaOrgIdFull.size() > 0) {
            roleVo.setOrgId(Long.valueOf(jaOrgIdFull.get(jaOrgIdFull.size()-1).toString()));
        } else {
            roleVo.setOrgId(null);
        }
        this.update(roleVo);

        // 更新角色对应的菜单数据
        roleDao.deleteRoleMenuRef(roleVo.getId());
        roleVo.setMenuList(menuDao.getMenuListByIdFulls(roleVo.getMenuIdFulls()));
        List<MenuPo> menuList = roleVo.getMenuList();
        if (menuList != null && menuList.size() > 0) {
            for (MenuPo menuPo : menuList) {
                roleDao.addRoleMenuRef(roleVo.getId(), menuPo.getId());
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

    public Page<RoleVo> getListData(Map params, UserPo currentUser) {
        List<OrgPo> orgList = null;
        String roleCode = StringUtil.objToString(params.get("roleCode"));
        String roleName = StringUtil.objToString(params.get("roleName"));
        Long orgIdSelected = StringUtil.objToLong(params.get("orgId"));
        Long pageIndex = StringUtil.objToLong(params.get("pageIndex"));
        pageIndex = pageIndex==null?1:pageIndex;
        Long pageSize = StringUtil.objToLong(params.get("pageSize"));
        pageSize = pageSize==null? Constants.PAGE_SIZE_DEFAULT :pageSize;
        if (orgIdSelected != null) {
            orgList = Arrays.asList(orgDao.findTreeById(orgIdSelected));
        } else {
            orgList = orgDao.getOrgListByUser(currentUser);
        }

        return roleDao.getListData(pageIndex, pageSize, orgList, roleCode, roleName);
    }
}
