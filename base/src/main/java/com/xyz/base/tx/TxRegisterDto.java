package com.xyz.base.tx;

import java.util.List;

/**
 * 注册事务的DTO
 */
public class TxRegisterDto {

    private String txCode; // 事务代码

    private String txName; // 事务名称

    private String txCaller; // 事务发起者

    private long timeout = 1000*5; // 事务try操作的超时设置（单位毫秒），默认5秒钟

    private boolean isSerial = false; // 是否串行，默认并行执行

    private List<TxRegisterDto_CalleeDto> txCalleeData; // 被调用者的事务数据（包括路径和参数）

    public String getTxCode() {
        return txCode;
    }

    public void setTxCode(String txCode) {
        this.txCode = txCode;
    }

    public String getTxName() {
        return txName;
    }

    public void setTxName(String txName) {
        this.txName = txName;
    }

    public String getTxCaller() {
        return txCaller;
    }

    public void setTxCaller(String txCaller) {
        this.txCaller = txCaller;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isSerial() {
        return isSerial;
    }

    public void setSerial(boolean serial) {
        isSerial = serial;
    }

    public List<TxRegisterDto_CalleeDto> getTxCalleeData() {
        return txCalleeData;
    }

    public void setTxCalleeData(List<TxRegisterDto_CalleeDto> txCalleeData) {
        this.txCalleeData = txCalleeData;
    }

    class TxRegisterDto_CalleeDto {
        private String txUrl; // 被调用者的doTry地址

        private String params; // 被调用者的地址对应的参数，JSON格式

        public String getTxUrl() {
            return txUrl;
        }

        public void setTxUrl(String txUrl) {
            this.txUrl = txUrl;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }
    }
}
