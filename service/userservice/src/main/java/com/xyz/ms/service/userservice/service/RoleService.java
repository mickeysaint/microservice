package com.xyz.ms.service.userservice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xyz.base.common.Constants;
import com.xyz.base.common.Page;
import com.xyz.base.po.user.MenuPo;
import com.xyz.base.po.user.OrgPo;
import com.xyz.base.po.user.RolePo;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.service.BaseService;
import com.xyz.base.util.MapUtils;
import com.xyz.base.util.StringUtil;
import com.xyz.base.util.TreeUtils;
import com.xyz.base.vo.user.RoleVo;
import com.xyz.ms.service.userservice.dao.MenuDao;
import com.xyz.ms.service.userservice.dao.OrgDao;
import com.xyz.ms.service.userservice.dao.RoleDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleService extends BaseService<RolePo> {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrgDao orgDao;

    @Autowired
    public void setDao(RoleDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void addRole(RoleVo roleVo) {
        // 添加角色数据
        JSONArray jaOrgIdFull = JSONArray.parseArray(roleVo.getOrgIdFull());
        if (jaOrgIdFull != null && jaOrgIdFull.size() > 0) {
            roleVo.setOrgId(Long.valueOf(jaOrgIdFull.get(jaOrgIdFull.size()-1).toString()));
        } else {
            roleVo.setOrgId(null);
        }
        this.save(roleVo);

        // 添加角色对应的菜单数据
        JSONArray jaMenuIdFulls = JSONArray.parseArray(roleVo.getMenuIdFulls());
        roleVo.setMenuList(menuDao.getMenuListByIdFulls(jaMenuIdFulls));
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
        JSONArray jaOrgIdFull = JSONArray.parseArray(roleVo.getOrgIdFull());
        if (jaOrgIdFull != null && jaOrgIdFull.size() > 0) {
            roleVo.setOrgId(Long.valueOf(jaOrgIdFull.get(jaOrgIdFull.size()-1).toString()));
        } else {
            roleVo.setOrgId(null);
        }
        this.update(roleVo);

        // 更新角色对应的菜单数据
        roleDao.deleteRoleMenuRef(roleVo.getId());
        JSONArray jaMenuIdFulls = JSONArray.parseArray(roleVo.getMenuIdFulls());
        roleVo.setMenuList(menuDao.getMenuListByIdFulls(jaMenuIdFulls));
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
        String orgIdJaStr = StringUtil.objToString(params.get("orgId"));
        JSONArray jaOrgId = JSON.parseArray(orgIdJaStr);
        Long orgIdSelected = null;
        if (jaOrgId.size() > 0) {
            orgIdSelected = jaOrgId.getLong(jaOrgId.size()-1);
        }
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

    public List<MenuPo> getMenuTreeByRole(Long roleId) {
        String sql = "SELECT distinct A.ID, A.PARENT_ID, A.MENU_CODE, A.MENU_NAME, " +
                "A.MENU_SHOW_NAME, A.MENU_COMPONENT, A.ID_FULL, A.MENU_NAME_FULL " +
                "FROM tu_menu A " +
                "JOIN tu_role_menu B ON A.ID=B.MENU_ID " +
                "WHERE B.ROLE_ID=? ";
        List<MenuPo> menuListOfRole = menuDao.findBySql(sql, Arrays.asList(roleId));
        Set<Long> parentIds = new HashSet<>();
        if (menuListOfRole != null && menuListOfRole.size() > 0) {
            for (MenuPo menuPo : menuListOfRole) {
                String idFull = menuPo.getIdFull();
                JSONArray jaIds = JSON.parseArray(idFull);
                for (int i=0; i<jaIds.size(); i++) {
                    parentIds.add(jaIds.getLong(i));
                }
            }
        }

        if (parentIds.size() > 0) {
            sql = "SELECT A.ID, A.PARENT_ID, A.MENU_CODE, A.MENU_NAME, " +
                    "A.MENU_SHOW_NAME, A.MENU_COMPONENT, A.ID_FULL, A.MENU_NAME_FULL " +
                    "FROM tu_menu A " +
                    "where ID in (" + StringUtils.join(parentIds, ",") + ") ";
            List<MenuPo> menu2List = menuDao.findBySql(sql, Arrays.asList());
            Map<Long, MenuPo> mapping = MapUtils.convertList2Map(menuListOfRole, "id", Long.class, MenuPo.class);

            for (MenuPo menuPo : menu2List) {
                if (mapping.get(menuPo.getId()) == null) {
                    menuListOfRole.add(menuPo);
                }
            }
        }

        return TreeUtils.convertList2Tree(menuListOfRole);
    }
}
