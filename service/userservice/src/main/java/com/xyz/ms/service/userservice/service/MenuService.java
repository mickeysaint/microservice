package com.xyz.ms.service.userservice.service;

import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.user.MenuPo;
import com.xyz.base.service.BaseService;
import com.xyz.ms.service.userservice.dao.MenuDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService extends BaseService<MenuPo> {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    public void setDao(MenuDao dao) {
        this.dao = dao;
    }

    public MenuPo findTreeById(Long id) {
        return menuDao.findTreeById(id);
    }

    public String createMenuCode(MenuPo menuPo) {
        if (menuPo.getParentId() == null) {
            return "";
        }

        MenuPo parentMenuPo = findById(menuPo.getParentId());
        String parentMenuCode = parentMenuPo.getMenuCode();
        parentMenuCode = StringUtils.isEmpty(parentMenuCode)?"":parentMenuCode;
        MenuPo eg = new MenuPo();
        eg.setParentId(menuPo.getParentId());
        List<MenuPo> chilren = findByEg(eg, " order by MENU_CODE ");
        int currIndex = 1;
        if (chilren != null && chilren.size() > 0) {
            for (MenuPo c : chilren) {
                String cMenuCode = c.getMenuCode();
                String index = cMenuCode.substring(parentMenuCode.length());
                if (currIndex != Integer.valueOf(index)) {
                    break;
                }
                currIndex++;
            }
        }
        if (currIndex >= 100) {
            throw new BusinessException("达到最大子节点数量");
        }
        return parentMenuCode + StringUtils.leftPad(Integer.toString(currIndex), 2,'0');
    }

    public String getMenuNameFull(Long id) {
        if (id == null) {
            return "";
        }

        MenuPo menuPo = this.findById(id);
        if (menuPo == null) {
            return "";
        } else {
            String parentMenuNameFull = getMenuNameFull(menuPo.getParentId());
            if (StringUtils.isEmpty(parentMenuNameFull)) {
                return menuPo.getMenuName();
            } else {
                return parentMenuNameFull + "/" + menuPo.getMenuName();
            }
        }
    }

    public String getIdFull(Long id) {
        if (id == null) {
            return "";
        }

        MenuPo menuPo = this.findById(id);
        if (menuPo == null) {
            return "";
        } else {
            String parentIdFull = getIdFull(menuPo.getParentId());
            if (StringUtils.isEmpty(parentIdFull)) {
                return menuPo.getId().toString();
            } else {
                return parentIdFull + "," + menuPo.getId();
            }
        }
    }
}
