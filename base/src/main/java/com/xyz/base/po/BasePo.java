package com.xyz.base.po;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public abstract class BasePo implements Serializable {

    public abstract Long getId();

    public abstract void setId(Long id);

    public String toString() {
        return JSON.toJSONString(this);
    }
}
