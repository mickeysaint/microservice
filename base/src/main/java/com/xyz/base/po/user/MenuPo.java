package com.xyz.base.po.user;

import com.xyz.base.po.BasePo;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="tu_menu")
public class MenuPo extends BasePo {

    @Column(name="ID")
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
