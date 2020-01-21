package com.xyz.ms.service.userservice.controller;

import com.alibaba.fastjson.JSONArray;
import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.user.MenuPo;
import com.xyz.base.util.AssertUtils;
import com.xyz.base.util.TreeUtils;
import com.xyz.ms.service.userservice.service.MenuService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    Logger logger = LoggerFactory.getLogger(MenuController.class);

    @RequestMapping("/save")
    public ResultBean<Void> save(@RequestBody MenuPo menuPo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(menuPo != null, "没有接收到菜单数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(menuPo.getMenuName()), "菜单名称不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(menuPo.getMenuShowName()), "菜单显示名称不能为空。");

            if (menuPo.getId() != null) {
                MenuPo editPo = menuService.findById(menuPo.getId());
                editPo.setMenuName(menuPo.getMenuName());
                editPo.setMenuNameFull(menuService.getMenuNameFull(menuPo.getId()));
                editPo.setIdFull("["+menuService.getIdFull(menuPo.getId())+"]");
                menuService.update(editPo);
            } else {
                String menuCode = menuService.createMenuCode(menuPo);
                menuPo.setMenuCode(menuCode);
                menuService.save(menuPo);

                menuPo.setMenuNameFull(menuService.getMenuNameFull(menuPo.getId()));
                menuPo.setIdFull("["+menuService.getIdFull(menuPo.getId())+"]");
                menuService.update(menuPo);
            }
        } catch(BusinessException e) {
            logger.error("保存菜单出错", e);
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            logger.error("保存菜单出错", e);
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
            menuService.deleteByIds(ids.toJavaList(Long.class));
        } catch(BusinessException e) {
            logger.error("删除菜单出错", e);
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            logger.error("删除菜单出错", e);
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/findById")
    public ResultBean<MenuPo> findById(@RequestParam(name="id") Long id) {
        ResultBean<MenuPo> ret = new ResultBean<>();
        MenuPo menuPo = menuService.findById(id);
        ret.setData(menuPo);
        return ret;
    }

    @RequestMapping("/findTreeById")
    public ResultBean<List<MenuPo>> findTreeById(@RequestParam(name="id") Long id) {
        ResultBean<List<MenuPo>> ret = new ResultBean<>();
        MenuPo menuPo = menuService.findTreeById(id);
        List<MenuPo> menuList = new ArrayList<>();
        if (menuPo != null) {
            menuList.add(menuPo);
        }
        TreeUtils.clearEmptyChildren(menuList);
        ret.setData(menuList);
        return ret;
    }

    @RequestMapping("/getListDataByParentId")
    public ResultBean<List<MenuPo>> getListDataByParentId(@RequestParam(name="parentId") Long parentId) {
        ResultBean<List<MenuPo>> ret = new ResultBean<>();
        MenuPo eg = new MenuPo();
        eg.setParentId(parentId);
        List<MenuPo> menuList = menuService.findByEg(eg, " ORDER BY MENU_CODE ");
        ret.setData(menuList);
        return ret;
    }
}
