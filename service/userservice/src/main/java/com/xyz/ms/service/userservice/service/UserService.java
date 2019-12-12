package com.xyz.ms.service.userservice.service;

import com.xyz.base.po.user.MenuPo;
import com.xyz.base.po.user.RolePo;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.service.BaseService;
import com.xyz.ms.service.userservice.dao.MenuDao;
import com.xyz.ms.service.userservice.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends BaseService<UserPo> {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

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

        UserPo ret = null;
        if (userList != null && userList.size() > 0) {
            ret = userList.get(0);

            // 设置用户的角色
            List<RolePo> roleList = roleDao.getRoleListByUser(ret);

            // 设置用户的菜单
            List<MenuPo> menuList = menuDao.getMenuListByRole(roleList);
            List<MenuPo> newMenuList = new ArrayList<>();
            if (menuList != null && menuList.size() > 0) {
                for (MenuPo menoPo : menuList) {
                    if (menoPo.getChildren() != null && menoPo.getChildren().size() > 0) {
                        newMenuList.add(menoPo);
                    }
                }
            }
            ret.setMenuList(newMenuList);
        }

        return ret;
    }

}
