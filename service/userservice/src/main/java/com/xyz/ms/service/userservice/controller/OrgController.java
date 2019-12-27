package com.xyz.ms.service.userservice.controller;

import com.alibaba.fastjson.JSONArray;
import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.user.OrgPo;
import com.xyz.base.util.AssertUtils;
import com.xyz.ms.service.userservice.service.OrgService;
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
@RequestMapping("/org")
public class OrgController {

    @Autowired
    private OrgService orgService;

    Logger logger = LoggerFactory.getLogger(OrgController.class);

    @RequestMapping("/save")
    public ResultBean<Void> save(@RequestBody OrgPo orgPo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(orgPo != null, "没有接收到组织数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(orgPo.getOrgName()), "组织名称不能为空。");

            if (orgPo.getId() != null) {
                OrgPo editPo = orgService.findById(orgPo.getId());
                editPo.setOrgName(orgPo.getOrgName());
                editPo.setOrgNameFull(orgService.getOrgNameFull(orgPo.getId()));
                editPo.setIdFull("["+orgService.getIdFull(orgPo.getId())+"]");
                orgService.update(editPo);
            } else {
                String orgCode = orgService.createOrgCode(orgPo);
                orgPo.setOrgCode(orgCode);
                orgService.save(orgPo);
                orgPo.setOrgNameFull(orgService.getOrgNameFull(orgPo.getId()));
                orgPo.setIdFull("["+orgService.getIdFull(orgPo.getId())+"]");
                orgService.update(orgPo);
            }
        } catch(BusinessException e) {
            logger.error("保存组织结构出错", e);
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            logger.error("保存组织结构出错", e);
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
            orgService.deleteByIds(ids.toJavaList(Long.class));
        } catch(BusinessException e) {
            logger.error("删除组织结构出错", e);
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            logger.error("删除组织结构出错", e);
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/findById")
    public ResultBean<OrgPo> findById(@RequestParam(name="id") Long id) {
        ResultBean<OrgPo> ret = new ResultBean<>();
        OrgPo orgPo = orgService.findById(id);
        ret.setData(orgPo);
        return ret;
    }

    @RequestMapping("/findTreeById")
    public ResultBean<List<OrgPo>> findTreeById(@RequestParam(name="id") Long id) {
        ResultBean<List<OrgPo>> ret = new ResultBean<>();
        OrgPo orgPo = orgService.findTreeById(id);
        List<OrgPo> orgList = new ArrayList<>();
        if (orgPo != null) {
            orgList.add(orgPo);
        }
        ret.setData(orgList);
        return ret;
    }

    @RequestMapping("/getTreeDataByParentId")
    public ResultBean<List<OrgPo>> getTreeDataByParentId(@RequestParam(name="parentId") Long parentId) {
        ResultBean<List<OrgPo>> ret = new ResultBean<>();
        OrgPo orgPo = orgService.findTreeById(parentId);
        ret.setData(orgPo.getChildren());
        return ret;
    }

    @RequestMapping("/getListDataByParentId")
    public ResultBean<List<OrgPo>> getListDataByParentId(@RequestParam(name="parentId") Long parentId) {
        ResultBean<List<OrgPo>> ret = new ResultBean<>();
        OrgPo eg = new OrgPo();
        eg.setParentId(parentId);
        List<OrgPo> orgList = orgService.findByEg(eg, " ORDER BY ORG_CODE ");
        ret.setData(orgList);
        return ret;
    }
}
