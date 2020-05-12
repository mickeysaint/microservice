package com.xyz.ms.service.userservice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xyz.base.common.Constants;
import com.xyz.base.common.Page;
import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.user.MenuPo;
import com.xyz.base.po.user.OrgPo;
import com.xyz.base.po.user.RolePo;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.service.BaseDao;
import com.xyz.base.service.BaseService;
import com.xyz.base.util.StringUtil;
import com.xyz.base.vo.user.UserVo;
import com.xyz.ms.service.userservice.client.Oauth2ClientService;
import com.xyz.ms.service.userservice.dao.MenuDao;
import com.xyz.ms.service.userservice.dao.OrgDao;
import com.xyz.ms.service.userservice.dao.RoleDao;
import com.xyz.ms.service.userservice.dao.UserDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<UserPo> {

    @Autowired
    private Oauth2ClientService oauth2ClientService;

    @Autowired
    private OrgDao orgDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    public void setDao(UserDao dao) {
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
            ret.setRoleList(roleList);

            // 设置用户的菜单
            List<MenuPo> menuList = menuDao.getMenuListByRole(roleList);
            ret.setMenuList(menuList);

            // 获取用户所属的组织
            List<OrgPo> orgList = orgDao.getOrgListByUser(ret);
            ret.setOrgList(orgList);
        }

        return ret;
    }

    public UserPo getCurrentUser(HttpServletRequest request) {
        UserPo ret = null;
        String accessToken = request.getHeader("accessToken");
        if (!StringUtils.isEmpty(accessToken)) {
            ResultBean<UserPo> resultBean = oauth2ClientService.getUserByToken(accessToken);
            ret = resultBean.getData();
        }

        if (ret == null) {
            throw new BusinessException("获取当前用户失败。");
        }
        return ret;
    }

    public Page<UserVo> getListData(Map params, UserPo currentUser) {
        List<OrgPo> orgList = null;
        String username = StringUtil.objToString(params.get("username"));
        String userFullName = StringUtil.objToString(params.get("userFullName"));
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

        return userDao.getListData(pageIndex, pageSize, orgList, username, userFullName);
    }

    @Transactional
    public void saveUser(UserVo userVo) {

        UserPo userPo = null;
        if (userVo.getId() == null) { // 新建
            userPo = new UserPo();
            userPo.setUsername(userVo.getUsername());
            userPo.setUserFullName(userVo.getUserFullName());
            userPo.setPassword(new BCryptPasswordEncoder().encode(userVo.getPassword()));
            this.save(userPo);

            userDao.addOrgUser(userPo.getId(), userVo.getOrgIds()); // 建立组织与用户的关联关系
            userDao.addRoleUser(userPo.getId(), userVo.getRoleIds()); // 建立角色与用户的关联关系
        } else { // 更新
            userPo = findById(userVo.getId());
            userPo.setUserFullName(userVo.getUserFullName());
            this.update(userPo);

            userDao.deleteOrgUser(userVo.getId()); // 删除用户关联的组织
            userDao.addOrgUser(userVo.getId(), userVo.getOrgIds()); // 重新建立组织与用户的关联关系

            userDao.deleteRoleUser(userVo.getId()); // 删除用户关联的角色
            userDao.addRoleUser(userVo.getId(), userVo.getRoleIds()); // 重新建立角色与用户的关联关系
        }

    }

    @Transactional
    public void deleteUserByIds(List<Long> userIdList) {
        if (userIdList == null || userIdList.size() == 0) {
            return;
        }

        for (Long userId : userIdList) {
            userDao.deleteOrgUser(userId); // 删除用户关联的组织
            userDao.deleteRoleUser(userId); // 删除用户关联的角色
        }
        userDao.deleteByIds(userIdList);
    }
}
