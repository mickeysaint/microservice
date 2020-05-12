package com.xyz.ms.service.baseservice.controller;

import com.alibaba.fastjson.JSONArray;
import com.xyz.base.common.Page;
import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.base.DictTypePo;
import com.xyz.base.util.AssertUtils;
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
@RequestMapping("/dictType")
public class DictTypeController {

    Logger logger = LoggerFactory.getLogger(DictTypeController.class);

    @Autowired
    private DictTypeService dictTypeService;

    @RequestMapping("/getListData")
    public ResultBean<Page<DictTypePo>> getListData(@RequestBody Map params, HttpServletRequest request) {
        ResultBean<Page<DictTypePo>> ret = new ResultBean<>();
        Page<DictTypePo> dictTypes = dictTypeService.getListData(params);
        ret.setData(dictTypes);
        return ret;
    }

    @RequestMapping("/delete")
    public ResultBean<Void> delete(@RequestBody JSONArray ids) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(ids.size() > 0, "待删除数据不能为空。");
            dictTypeService.deleteByIds(ids.toJavaList(Long.class));
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
    public ResultBean<Void> save(@RequestBody DictTypePo dictTypePo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(dictTypePo != null, "没有接收到数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictTypePo.getTypeCode()), "字典类型编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictTypePo.getTypeName()), "字典类型名称不能为空。");

            AssertUtils.isTrue(
                    (
                            (dictTypePo.getId() != null)
                                    || (dictTypePo.getId() == null && !dictTypeService.exists(dictTypePo.getTypeCode()))
                    ),
                    "该字典类型已经存在。");

            if (dictTypePo.getId() != null) {
                DictTypePo editPo = dictTypeService.findById(dictTypePo.getId());
                editPo.setTypeCode(dictTypePo.getTypeCode());
                editPo.setTypeName(dictTypePo.getTypeName());
                dictTypeService.update(editPo);
            } else {
                dictTypeService.save(dictTypePo);
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
