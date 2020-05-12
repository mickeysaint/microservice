package com.xyz.base.vo.user;

import com.xyz.base.po.base.DictEntryPo;

import javax.persistence.Column;

public class DictEntryVo extends DictEntryPo {

    @Column(name="TYPE_NAME")
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
