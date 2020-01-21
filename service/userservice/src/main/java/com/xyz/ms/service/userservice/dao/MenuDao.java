package com.xyz.ms.service.userservice.dao;

import com.alibaba.fastjson.JSONArray;
import com.xyz.base.po.user.MenuPo;
import com.xyz.base.po.user.RolePo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.util.TreeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("menuDao")
public class MenuDao extends BaseDao<MenuPo> {

    public List<MenuPo> getMenuListByRole(List<RolePo> roleList) {

        if (roleList == null || roleList.size() == 0) {
            return null;
        }

        String sql = "SELECT DISTINCT A.ID, A.PARENT_ID, " +
                "A.MENU_CODE, A.MENU_NAME, A.MENU_SHOW_NAME, A.MENU_COMPONENT " +
                "FROM tu_menu A " +
                "JOIN tu_role_menu B ON A.ID=B.MENU_ID " +
                "WHERE B.ROLE_ID IN (%s) ORDER BY A.MENU_CODE ASC ";

        List<String> qlist = new ArrayList<>();
        List<Long> roleIdList = new ArrayList<>();
        for (RolePo rolePo : roleList) {
            qlist.add("?");
            roleIdList.add(rolePo.getId());
        }

        sql = String.format(sql, StringUtils.join(qlist, ","));
        List<MenuPo> menuList = this.findBySql(sql, Arrays.asList(roleIdList.toArray()));
        List<MenuPo> retList = TreeUtils.convertList2Tree(menuList);

        return retList;
    }

    public MenuPo findTreeById(Long id) {
        MenuPo menuPo = findById(id);
        String parentMenuCode = StringUtils.isEmpty(menuPo.getMenuCode())?"":menuPo.getMenuCode();
        String sql = "SELECT A.ID, A.MENU_CODE, A.MENU_NAME, A.PARENT_ID FROM TU_MENU A " +
                "WHERE A.MENU_CODE LIKE ? ORDER BY A.MENU_CODE ASC ";
        List<MenuPo> list = this.findBySql(sql, Arrays.asList(parentMenuCode + "%"));
        List<MenuPo> children = TreeUtils.convertList2Tree(list, id);
        menuPo.setChildren(children);
        return menuPo;
    }

    public List<MenuPo> getMenuListByIdFulls(JSONArray idFulls) {
        if (idFulls == null || idFulls.size() == 0) {
            return null;
        }

        Set<Long> menuIds = new HashSet<Long>();
        for (int i=0; i<idFulls.size(); i++) {
            JSONArray jaIdFull = idFulls.getJSONArray(i);
            if (jaIdFull.size() > 0) {
                for (int j=0; j<jaIdFull.size(); j++) {
                    menuIds.add(jaIdFull.getLong(j));
                }
            }
        }

        List<MenuPo> menuPoList = new ArrayList<MenuPo>();
        for (Long menuId : menuIds) {
            menuPoList.add(findById(menuId));
        }

        return menuPoList;
    }
}
