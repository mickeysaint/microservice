package com.xyz.ms.service.userservice.controller;

import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.user.OrgPo;
import com.xyz.base.util.AssertUtils;
import com.xyz.ms.service.userservice.service.OrgService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/org")
public class OrgController {

    @Autowired
    private OrgService orgService;

    @RequestMapping("/add")
    public ResultBean<Void> add(OrgPo orgPo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(orgPo != null, "没有接收到组织数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(orgPo.getOrgName()), "组织名称不能为空。");
            String orgCode = orgService.createOrgCode(orgPo);
            orgPo.setOrgCode(orgCode);
            orgService.save(orgPo);
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
    public ResultBean<OrgPo> findById(Long id) {
        ResultBean<OrgPo> ret = new ResultBean<>();
        OrgPo orgPo = orgService.findById(id);
        ret.setData(orgPo);
        return ret;
    }

    @RequestMapping("/findTreeById")
    public ResultBean<OrgPo> findTreeById(Long id) {
        ResultBean<OrgPo> ret = new ResultBean<>();
        OrgPo orgPo = orgService.findTreeById(id);
        ret.setData(orgPo);
        return ret;
    }
}
