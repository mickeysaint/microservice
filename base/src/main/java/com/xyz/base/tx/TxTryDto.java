package com.xyz.base.tx;

public class TxTryDto {

    private String txId; // 事务UUID

    private String txCode; // 事务代码

    private String txName; // 事务名称

    private String txData; // try操作关联的数据（JSON格式）

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

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

    public String getTxData() {
        return txData;
    }

    public void setTxData(String txData) {
        this.txData = txData;
    }
}
