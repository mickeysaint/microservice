package com.xyz.ms.service.baseservice.controller;

import com.alibaba.fastjson.JSONArray;
import com.xyz.base.common.Page;
import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.base.DictEntryPo;
import com.xyz.base.util.AssertUtils;
import com.xyz.base.vo.user.DictEntryVo;
import com.xyz.ms.service.baseservice.service.DictEntryService;
import com.xyz.ms.service.baseservice.service.DictTypeService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/dictEntry")
public class DictEntryController {

    Logger logger = LoggerFactory.getLogger(DictEntryController.class);

    @Autowired
    private DictTypeService dictTypeService;

    @Autowired
    private DictEntryService dictEntryService;

    @RequestMapping("/getListData")
    public ResultBean<Page<DictEntryVo>> getListData(@RequestBody Map params, HttpServletRequest request) {
        ResultBean<Page<DictEntryVo>> ret = new ResultBean<>();
        Page<DictEntryVo> dictEntrys = dictEntryService.getListData(params);
        ret.setData(dictEntrys);
        return ret;
    }

    @RequestMapping("/delete")
    public ResultBean<Void> delete(@RequestBody JSONArray ids) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(ids.size() > 0, "待删除数据不能为空。");
            dictEntryService.deleteByIds(ids.toJavaList(Long.class));
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

    @RequestMapping("/save")
    public ResultBean<Void> save(@RequestBody DictEntryPo dictEntryPo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(dictEntryPo != null, "没有接收到数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntryPo.getTypeCode()), "字典类型编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntryPo.getEntryCode()), "字典项代码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntryPo.getEntryName()), "字典项名称不能为空。");

            if (dictEntryPo.getId() == null) {
                AssertUtils.isTrue(dictTypeService.exists(dictEntryPo.getTypeCode()),
                        "该字典类型不存在。");

                AssertUtils.isTrue(!dictEntryService.exists(dictEntryPo.getTypeCode(), dictEntryPo.getEntryCode()),
                        "该字典项已经存在。");
            }

            if (dictEntryPo.getId() != null) {
                DictEntryPo editPo = dictEntryService.findById(dictEntryPo.getId());
                editPo.setTypeCode(dictEntryPo.getTypeCode());
                editPo.setEntryCode(dictEntryPo.getEntryCode());
                editPo.setEntryName(dictEntryPo.getEntryName());
                dictEntryService.update(editPo);
            } else {
                dictEntryService.save(dictEntryPo);
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
