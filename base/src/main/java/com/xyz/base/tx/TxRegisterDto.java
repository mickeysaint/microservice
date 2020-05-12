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

    private String txStatus; // 事务状态   init/try_ok/try_failed/confirm_ok/cancel_ok

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

    public String getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(String txStatus) {
        this.txStatus = txStatus;
    }

    class TxRegisterDto_CalleeDto {
        private String txUrlTry; // 被调用者的doTry地址

        private String paramsTry; // 被调用者的地址对应的参数，JSON格式

        private String txUrlConfirm;

        private String paramsConfirm;

        private String txUrlCancel;

        private String paramsCancel;

        public String getTxUrlTry() {
            return txUrlTry;
        }

        public void setTxUrlTry(String txUrlTry) {
            this.txUrlTry = txUrlTry;
        }

        public String getParamsTry() {
            return paramsTry;
        }

        public void setParamsTry(String paramsTry) {
            this.paramsTry = paramsTry;
        }

        public String getTxUrlConfirm() {
            return txUrlConfirm;
        }

        public void setTxUrlConfirm(String txUrlConfirm) {
            this.txUrlConfirm = txUrlConfirm;
        }

        public String getParamsConfirm() {
            return paramsConfirm;
        }

        public void setParamsConfirm(String paramsConfirm) {
            this.paramsConfirm = paramsConfirm;
        }

        public String getTxUrlCancel() {
            return txUrlCancel;
        }

        public void setTxUrlCancel(String txUrlCancel) {
            this.txUrlCancel = txUrlCancel;
        }

        public String getParamsCancel() {
            return paramsCancel;
        }

        public void setParamsCancel(String paramsCancel) {
            this.paramsCancel = paramsCancel;
        }
    }
}
