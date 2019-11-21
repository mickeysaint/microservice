package com.xyz.ms.service.baseservice.controller;

import com.xyz.base.dbutil.ResultBean;
import com.xyz.base.util.AssertUtils;
import com.xyz.base.util.BusinessException;
import com.xyz.ms.service.baseservice.bean.TbaseSysconfig;
import com.xyz.ms.service.baseservice.service.TbaseSysconfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysconfig")
public class SysconfigController {

    @Autowired
    private TbaseSysconfigService tbaseSysconfigService;

    @RequestMapping("/add")
    public ResultBean<Void> add(TbaseSysconfig sysconfig) {
        ResultBean<Void> ret = new ResultBean<>();

        try {
            AssertUtils.isTrue(sysconfig != null, "没有接收到待保存的数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(sysconfig.getConfigKey()), "配置项编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(sysconfig.getConfigName()), "配置项名称不能为空。");

            AssertUtils.isTrue(tbaseSysconfigService.exists(sysconfig.getConfigKey()), "该字典类型已经存在。");

            tbaseSysconfigService.save(sysconfig);
        } catch(BusinessException e) {
            ret.setResult(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setResult(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/getById")
    public ResultBean<TbaseSysconfig> getById(Long id) {
        ResultBean<TbaseSysconfig> ret = new ResultBean<>();
        TbaseSysconfig sysconfig = tbaseSysconfigService.findById(id);
        ret.setData(sysconfig);
        return ret;
    }

    @RequestMapping("/getByCode")
    public ResultBean<TbaseSysconfig> getByCode(String configKey) {
        ResultBean<TbaseSysconfig> ret = new ResultBean<>();
        TbaseSysconfig eg = new TbaseSysconfig();
        eg.setConfigKey(configKey);
        List<TbaseSysconfig> sysconfigList = tbaseSysconfigService.findByEg(eg);
        ret.setData(CollectionUtils.isEmpty(sysconfigList)?null:sysconfigList.get(0));
        return ret;
    }

    @RequestMapping("/update")
    public ResultBean<Void> update(TbaseSysconfig sysconfig) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(sysconfig != null, "没有接收到待保存的数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(sysconfig.getConfigKey()), "配置项编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(sysconfig.getConfigName()), "配置项名称不能为空。");

            TbaseSysconfig another = tbaseSysconfigService.findByConfigKey(sysconfig.getConfigKey());
            if (another != null && !another.getId().equals(sysconfig.getId())) {
                throw new BusinessException("配置项编码重复。");
            }

            tbaseSysconfigService.update(sysconfig);
        } catch(BusinessException e) {
            ret.setResult(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setResult(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/delete")
    public ResultBean<Void> delete(Long id) {
        TbaseSysconfig sysconfig = tbaseSysconfigService.findById(id);
        tbaseSysconfigService.delete(sysconfig);
        ResultBean<Void> ret = new ResultBean<Void>();
        return ret;
    }
}
