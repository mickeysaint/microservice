package com.xyz.ms.service.userservice.controller;

import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.user.MenuPo;
import com.xyz.base.util.AssertUtils;
import com.xyz.ms.service.userservice.service.MenuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/add")
    public ResultBean<Void> add(MenuPo menuPo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(menuPo != null, "没有接收到菜单数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(menuPo.getMenuName()), "菜单名称不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(menuPo.getMenuShowName()), "菜单显示名称不能为空。");
            String menuCode = menuService.createMenuCode(menuPo);
            menuPo.setMenuCode(menuCode);
            menuService.save(menuPo);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/findById")
    public ResultBean<MenuPo> findById(Long id) {
        ResultBean<MenuPo> ret = new ResultBean<>();
        MenuPo menuPo = menuService.findById(id);
        ret.setData(menuPo);
        return ret;
    }

    @RequestMapping("/findTreeById")
    public ResultBean<MenuPo> findTreeById(Long id) {
        ResultBean<MenuPo> ret = new ResultBean<>();
        MenuPo menuPo = menuService.findTreeById(id);
        ret.setData(menuPo);
        return ret;
    }
}
