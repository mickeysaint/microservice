package com.xyz.ms.service.baseservice.controller;

import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.base.SysConfigPo;
import com.xyz.base.util.AssertUtils;
import com.xyz.ms.service.baseservice.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysconfig")
public class SysconfigController {

    @Autowired
    private SysConfigService sysConfigService;

    @RequestMapping("/add")
    public ResultBean<Void> add(SysConfigPo sysconfig) {
        ResultBean<Void> ret = new ResultBean<>();

        try {
            AssertUtils.isTrue(sysconfig != null, "没有接收到待保存的数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(sysconfig.getConfigKey()), "配置项编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(sysconfig.getConfigName()), "配置项名称不能为空。");

            AssertUtils.isTrue(sysConfigService.exists(sysconfig.getConfigKey()), "该字典类型已经存在。");

            sysConfigService.save(sysconfig);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

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

    @RequestMapping("/update")
    public ResultBean<Void> update(SysConfigPo sysConfigPo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(sysConfigPo != null, "没有接收到待保存的数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(sysConfigPo.getConfigKey()), "配置项编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(sysConfigPo.getConfigName()), "配置项名称不能为空。");

            SysConfigPo another = sysConfigService.findByConfigKey(sysConfigPo.getConfigKey());
            if (another != null && !another.getId().equals(sysConfigPo.getId())) {
                throw new BusinessException("配置项编码重复。");
            }

            sysConfigService.update(sysConfigPo);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/delete")
    public ResultBean<Void> delete(Long id) {
        SysConfigPo sysconfig = sysConfigService.findById(id);
        sysConfigService.delete(sysconfig);
        ResultBean<Void> ret = new ResultBean<Void>();
        return ret;
    }
}
