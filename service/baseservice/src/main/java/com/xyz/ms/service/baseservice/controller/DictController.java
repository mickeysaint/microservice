package com.xyz.ms.service.baseservice.controller;

import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.base.DictEntryPo;
import com.xyz.base.po.base.DictTypePo;
import com.xyz.base.util.AssertUtils;
import com.xyz.ms.service.baseservice.service.DictEntryService;
import com.xyz.ms.service.baseservice.service.DictTypeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictTypeService dictTypeService;

    @Autowired
    private DictEntryService dictEntryService;

    @RequestMapping("/addType")
    public ResultBean<Void> addType(DictTypePo dictTypePo) {
        ResultBean<Void> ret = new ResultBean<>();

        try {
            AssertUtils.isTrue(dictTypePo != null, "没有接收到待保存的数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictTypePo.getTypeCode()), "字典类型编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictTypePo.getTypeName()), "字典类型名称不能为空。");

            AssertUtils.isTrue(dictTypeService.exists(dictTypePo.getTypeCode()), "该字典类型已经存在。");

            dictTypeService.save(dictTypePo);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/getTypeById")
    public ResultBean<DictTypePo> getTypeById(Long id) {
        ResultBean<DictTypePo> ret = new ResultBean<>();
        DictTypePo dictTypePo = dictTypeService.findById(id);
        ret.setData(dictTypePo);
        return ret;
    }

    @RequestMapping("/getTypeByCode")
    public ResultBean<DictTypePo> getTypeByCode(String typeCode) {
        ResultBean<DictTypePo> ret = new ResultBean<>();
        DictTypePo eg = new DictTypePo();
        eg.setTypeCode(typeCode);
        List<DictTypePo> dictTypeList = dictTypeService.findByEg(eg);
        ret.setData(CollectionUtils.isEmpty(dictTypeList)?null:dictTypeList.get(0));
        return ret;
    }

    @RequestMapping("/updateType")
    public ResultBean<Void> updateType(DictTypePo dictTypePo) {
        ResultBean<Void> ret = new ResultBean<>();

        try {
            AssertUtils.isTrue(dictTypePo != null, "没有接收到待保存的数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictTypePo.getTypeCode()), "字典类型编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictTypePo.getTypeName()), "字典类型名称不能为空。");

            DictTypePo another = dictTypeService.findByCode(dictTypePo.getTypeCode());
            if (another != null && !another.getId().equals(dictTypePo.getId())) {
                throw new BusinessException("字典类型编码重复。");
            }

            dictTypeService.update(dictTypePo);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/deleteType")
    public ResultBean<Void> deleteType(Long id) {
        DictTypePo dictTypePo = dictTypeService.findById(id);
        dictTypeService.delete(dictTypePo);
        ResultBean<Void> ret = new ResultBean<Void>();
        return ret;
    }

    @RequestMapping("/addEntry")
    public ResultBean<Void> addEntry(DictEntryPo dictEntryPo) {
        ResultBean<Void> ret = new ResultBean<>();

        try {
            AssertUtils.isTrue(dictEntryPo != null, "没有接收到待保存的数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntryPo.getTypeCode()), "字典类型编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntryPo.getEntryCode()), "字典项编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntryPo.getEntryName()), "字典项名称不能为空。");

            AssertUtils.isTrue(dictEntryService.exists(dictEntryPo.getTypeCode(), dictEntryPo.getEntryCode()), "该字典项已经存在。");

            dictEntryService.save(dictEntryPo);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/getEntryById")
    public ResultBean<DictEntryPo> getEntryById(Long id) {
        ResultBean<DictEntryPo> ret = new ResultBean<>();
        DictEntryPo dictEntryPo = dictEntryService.findById(id);
        ret.setData(dictEntryPo);
        return ret;
    }

    @RequestMapping("/getEntryByCode")
    public ResultBean<DictEntryPo> getEntryByCode(String typeCode, String entryCode) {
        ResultBean<DictEntryPo> ret = new ResultBean<>();
        DictEntryPo eg = new DictEntryPo();
        eg.setTypeCode(typeCode);
        eg.setEntryCode(entryCode);
        List<DictEntryPo> dictEntryList = dictEntryService.findByEg(eg);
        ret.setData(CollectionUtils.isEmpty(dictEntryList)?null:dictEntryList.get(0));
        return ret;
    }

    @RequestMapping("/updateEntry")
    public ResultBean<Void> updateEntry(DictEntryPo dictEntry) {
        ResultBean<Void> ret = new ResultBean<>();

        try {
            AssertUtils.isTrue(dictEntry != null, "没有接收到待保存的数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntry.getTypeCode()), "字典类型编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntry.getEntryCode()), "字典项编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntry.getEntryName()), "字典项名称不能为空。");

            DictEntryPo another = dictEntryService.findByCode(dictEntry.getTypeCode(), dictEntry.getEntryCode());
            if (another != null && !another.getId().equals(dictEntry.getId())) {
                throw new BusinessException("字典项编码重复。");
            }

            dictEntryService.update(dictEntry);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/deleteEntry")
    public ResultBean<Void> deleteEntry(Long id) {
        DictEntryPo dictEntry = dictEntryService.findById(id);
        dictEntryService.delete(dictEntry);
        ResultBean<Void> ret = new ResultBean<>();
        return ret;
    }
}
