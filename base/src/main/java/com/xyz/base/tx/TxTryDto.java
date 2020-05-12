package com.xyz.base.tx;

/**
 * try 为本事务预留资源
 */
public class TxTryDto {

    private String txId; // 事务UUID

    private String txCode; // 事务代码

    private String txName; // 事务名称

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
}
