package com.xyz.base.po.base;

import com.xyz.base.po.BasePo;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="tb_dict_entry")
public class DictEntryPo extends BasePo {

    @Column(name="ID")
    private Long id;

    @Column(name="TYPE_CODE")
    private String typeCode;

    @Column(name="ENTRY_CODE")
    private String entryCode;

    @Column(name="ENTRY_NAME")
    private String entryName;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
