package com.xyz.base.po.base;

import com.xyz.base.po.BasePo;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="tb_dict_type")
public class DictTypePo extends BasePo {

    @Column(name="ID")
    private Long id;

    @Column(name="TYPE_CODE")
    private String typeCode;

    @Column(name="TYPE_NAME")
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
