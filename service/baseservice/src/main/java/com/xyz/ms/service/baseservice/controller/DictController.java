package com.xyz.ms.service.baseservice.controller;

import com.xyz.base.dbutil.ResultBean;
import com.xyz.base.util.AssertUtils;
import com.xyz.base.util.BusinessException;
import com.xyz.ms.service.baseservice.bean.TbaseDictEntry;
import com.xyz.ms.service.baseservice.bean.TbaseDictType;
import com.xyz.ms.service.baseservice.service.TbaseDictEntryService;
import com.xyz.ms.service.baseservice.service.TbaseDictTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private TbaseDictTypeService tbaseDictTypeService;

    @Autowired
    private TbaseDictEntryService tbaseDictEntryService;

    @RequestMapping("/addType")
    public ResultBean<Void> addType(TbaseDictType dictType) {
        ResultBean<Void> ret = new ResultBean<>();

        try {
            AssertUtils.isTrue(dictType != null, "没有接收到待保存的数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictType.getTypeCode()), "字典类型编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictType.getTypeName()), "字典类型名称不能为空。");

            AssertUtils.isTrue(tbaseDictTypeService.exists(dictType.getTypeCode()), "该字典类型已经存在。");

            tbaseDictTypeService.save(dictType);
        } catch(BusinessException e) {
            ret.setResult(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setResult(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/getTypeById")
    public ResultBean<TbaseDictType> getTypeById(Long id) {
        ResultBean<TbaseDictType> ret = new ResultBean<>();
        TbaseDictType dictType = tbaseDictTypeService.findById(id);
        ret.setData(dictType);
        return ret;
    }

    @RequestMapping("/getTypeByCode")
    public ResultBean<TbaseDictType> getTypeByCode(String typeCode) {
        ResultBean<TbaseDictType> ret = new ResultBean<>();
        TbaseDictType eg = new TbaseDictType();
        eg.setTypeCode(typeCode);
        List<TbaseDictType> dictTypeList = tbaseDictTypeService.findByEg(eg);
        ret.setData(CollectionUtils.isEmpty(dictTypeList)?null:dictTypeList.get(0));
        return ret;
    }

    @RequestMapping("/updateType")
    public ResultBean<Void> updateType(TbaseDictType dictType) {
        ResultBean<Void> ret = new ResultBean<>();

        try {
            AssertUtils.isTrue(dictType != null, "没有接收到待保存的数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictType.getTypeCode()), "字典类型编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictType.getTypeName()), "字典类型名称不能为空。");

            TbaseDictType another = tbaseDictTypeService.findByCode(dictType.getTypeCode());
            if (another != null && !another.getId().equals(dictType.getId())) {
                throw new BusinessException("字典类型编码重复。");
            }

            tbaseDictTypeService.update(dictType);
        } catch(BusinessException e) {
            ret.setResult(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setResult(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/deleteType")
    public ResultBean<Void> deleteType(Long id) {
        TbaseDictType dictType = tbaseDictTypeService.findById(id);
        tbaseDictTypeService.delete(dictType);
        ResultBean<Void> ret = new ResultBean<Void>();
        return ret;
    }

    @RequestMapping("/addEntry")
    public ResultBean<Void> addEntry(TbaseDictEntry dictEntry) {
        ResultBean<Void> ret = new ResultBean<>();

        try {
            AssertUtils.isTrue(dictEntry != null, "没有接收到待保存的数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntry.getTypeCode()), "字典类型编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntry.getEntryCode()), "字典项编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntry.getEntryName()), "字典项名称不能为空。");

            AssertUtils.isTrue(tbaseDictEntryService.exists(dictEntry.getTypeCode(), dictEntry.getEntryCode()), "该字典项已经存在。");

            tbaseDictEntryService.save(dictEntry);
        } catch(BusinessException e) {
            ret.setResult(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setResult(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/getEntryById")
    public ResultBean<TbaseDictEntry> getEntryById(Long id) {
        ResultBean<TbaseDictEntry> ret = new ResultBean<>();
        TbaseDictEntry dictEntry = tbaseDictEntryService.findById(id);
        ret.setData(dictEntry);
        return ret;
    }

    @RequestMapping("/getEntryByCode")
    public ResultBean<TbaseDictEntry> getEntryByCode(String typeCode, String entryCode) {
        ResultBean<TbaseDictEntry> ret = new ResultBean<>();
        TbaseDictEntry eg = new TbaseDictEntry();
        eg.setTypeCode(typeCode);
        eg.setEntryCode(entryCode);
        List<TbaseDictEntry> dictEntryList = tbaseDictEntryService.findByEg(eg);
        ret.setData(CollectionUtils.isEmpty(dictEntryList)?null:dictEntryList.get(0));
        return ret;
    }

    @RequestMapping("/updateEntry")
    public ResultBean<Void> updateEntry(TbaseDictEntry dictEntry) {
        ResultBean<Void> ret = new ResultBean<>();

        try {
            AssertUtils.isTrue(dictEntry != null, "没有接收到待保存的数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntry.getTypeCode()), "字典类型编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntry.getEntryCode()), "字典项编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(dictEntry.getEntryName()), "字典项名称不能为空。");

            TbaseDictEntry another = tbaseDictEntryService.findByCode(dictEntry.getTypeCode(), dictEntry.getEntryCode());
            if (another != null && !another.getId().equals(dictEntry.getId())) {
                throw new BusinessException("字典项编码重复。");
            }

            tbaseDictEntryService.update(dictEntry);
        } catch(BusinessException e) {
            ret.setResult(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setResult(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/deleteEntry")
    public ResultBean<Void> deleteEntry(Long id) {
        TbaseDictEntry dictEntry = tbaseDictEntryService.findById(id);
        tbaseDictEntryService.delete(dictEntry);
        ResultBean<Void> ret = new ResultBean<Void>();
        return ret;
    }
}
