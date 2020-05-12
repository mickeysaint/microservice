package com.xyz.ms.service.baseservice.controller;

import com.alibaba.fastjson.JSONArray;
import com.xyz.base.common.Page;
import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.base.SysConfigPo;
import com.xyz.base.util.AssertUtils;
import com.xyz.ms.service.baseservice.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sysconfig")
public class SysconfigController {

    Logger logger = LoggerFactory.getLogger(SysconfigController.class);

    @Autowired
    private SysConfigService sysConfigService;

    @RequestMapping("/getById")
    public ResultBean<SysConfigPo> getById(Long id) {
        ResultBean<SysConfigPo> ret = new ResultBean<>();
        SysConfigPo sysconfig = sysConfigService.findById(id);
        ret.setData(sysconfig);
        return ret;
    }

    @RequestMapping("/getByCode")
    public ResultBean<SysConfigPo> getByCode(String configKey) {
        ResultBean<SysConfigPo> ret = new ResultBean<>();
        SysConfigPo eg = new SysConfigPo();
        eg.setConfigKey(configKey);
        List<SysConfigPo> sysconfigList = sysConfigService.findByEg(eg);
        ret.setData(CollectionUtils.isEmpty(sysconfigList)?null:sysconfigList.get(0));
        return ret;
    }

    @RequestMapping("/delete")
    public ResultBean<Void> delete(@RequestBody JSONArray ids) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(ids.size() > 0, "待删除数据不能为空。");
            sysConfigService.deleteByIds(ids.toJavaList(Long.class));
        } catch(BusinessException e) {
            logger.error("删除出错", e);
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            logger.error("删除出错", e);
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/getListData")
    public ResultBean<Page<SysConfigPo>> getListData(@RequestBody Map params, HttpServletRequest request) {
        ResultBean<Page<SysConfigPo>> ret = new ResultBean<>();
        Page<SysConfigPo> sysConfigs = sysConfigService.getListData(params);
        ret.setData(sysConfigs);
        return ret;
    }

    @RequestMapping("/save")
    public ResultBean<Void> save(@RequestBody SysConfigPo sysConfigPo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(sysConfigPo != null, "没有接收到数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(sysConfigPo.getConfigKey()), "参数代码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(sysConfigPo.getConfigName()), "参数名称不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(sysConfigPo.getConfigValue()), "参数值不能为空。");

            AssertUtils.isTrue(
                    (
                            (sysConfigPo.getId() != null)
                                    || (sysConfigPo.getId() == null && !sysConfigService.exists(sysConfigPo.getConfigKey()))
                    ),
                    "该参数已经存在。");

            if (sysConfigPo.getId() != null) {
                SysConfigPo editPo = sysConfigService.findById(sysConfigPo.getId());
                editPo.setConfigName(sysConfigPo.getConfigName());
                editPo.setConfigValue(sysConfigPo.getConfigValue());
                editPo.setRemark(sysConfigPo.getRemark());
                sysConfigService.update(editPo);
            } else {
                sysConfigService.save(sysConfigPo);
            }
        } catch(BusinessException e) {
            logger.error("保存出错", e);
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            logger.error("保存出错", e);
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }
}
