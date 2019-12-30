package com.xyz.ms.service.userservice.controller;

import com.alibaba.fastjson.JSONArray;
import com.xyz.base.common.Page;
import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.user.MenuPo;
import com.xyz.base.po.user.RolePo;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.util.AssertUtils;
import com.xyz.base.vo.user.RoleVo;
import com.xyz.ms.service.userservice.service.RoleService;
import com.xyz.ms.service.userservice.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(RoleController.class);

    @RequestMapping("/save")
    public ResultBean<Void> save(@RequestBody RoleVo roleVo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(roleVo != null, "没有接收到角色数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(roleVo.getRoleCode()), "角色代码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(roleVo.getRoleName()), "角色名称不能为空。");

            if (roleVo.getId() != null) {
                roleService.updateRole(roleVo);
            } else {
                roleService.addRole(roleVo);
            }
        } catch(BusinessException e) {
            logger.error("保存角色出错", e);
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            logger.error("保存角色出错", e);
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/delete")
    public ResultBean<Void> delete(@RequestBody JSONArray ids) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(ids.size() > 0, "待删除数据不能为空。");
            roleService.deleteRoleByIds(ids.toJavaList(Long.class));
        } catch(BusinessException e) {
            logger.error("删除角色出错", e);
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            logger.error("删除角色出错", e);
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/findById")
    public ResultBean<RolePo> findById(@RequestParam(name="id") Long id) {
        ResultBean<RolePo> ret = new ResultBean<>();
        RolePo rolePo = roleService.findRoleById(id);
        ret.setData(rolePo);
        return ret;
    }

    @RequestMapping("/getListData")
    public ResultBean<Page<RoleVo>> getListData(@RequestBody Map params, HttpServletRequest request) {
        UserPo currentUser = userService.getCurrentUser(request);
        ResultBean<Page<RoleVo>> ret = new ResultBean<>();
        Page<RoleVo> roles = roleService.getListData(params, currentUser);
        ret.setData(roles);
        return ret;
    }

    @RequestMapping("/getMenuTreeByRole")
    public ResultBean<List<MenuPo>> getMenuTreeByRole(@RequestParam Long roleId) {
        ResultBean<List<MenuPo>> ret = new ResultBean<>();
        List<MenuPo> menus = roleService.getMenuTreeByRole(roleId);
        ret.setData(menus);
        return ret;
    }
}
