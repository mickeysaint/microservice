package com.xyz.ms.service.baseservice.bean;

import com.xyz.base.dbutil.BaseEntity;

public class TbaseDictType extends BaseEntity {

    private String typeCode;

    private String typeName;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
