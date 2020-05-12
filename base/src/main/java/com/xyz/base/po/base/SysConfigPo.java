package com.xyz.base.po.base;

import com.xyz.base.po.BasePo;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="tb_sys_config")
public class SysConfigPo extends BasePo {

    @Column(name="ID")
    private Long id;

    @Column(name="CONFIG_KEY")
    private String configKey;

    @Column(name="CONFIG_NAME")
    private String configName;

    @Column(name="CONFIG_VALUE")
    private String configValue;

    @Column(name="REMARK")
    private String remark;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
