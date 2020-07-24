package com.xyz.base.po.tx;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xyz.base.po.BasePo;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name="tx_transaction")
public class TransactionPo extends BasePo {

    @Column(name="ID")
    private Long id;

    @Column(name="TX_ID")
    private String txId;

    @Column(name="TX_CODE")
    private String txCode;

    @Column(name="TX_NAME")
    private String txName;

    @Column(name="TX_CALLER_SERVICE")
    private String txCallerService;

    @Column(name="TX_CALLER")
    private String txCaller;

    @Column(name="TRY_TIMEOUT")
    private Long tryTimeout;

    @Column(name="IS_SERIAL")
    private String isSerial;

    @Column(name="TX_STATUS")
    private String txStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="CREATE_TIME")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="UPDATE_TIME")
    private Date updateTime;

    @Column(name="MESSAGE")
    private String message;

    @Column(name="EXEC_STATUS")
    private String execStatus;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

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

    public String getTxCallerService() {
        return txCallerService;
    }

    public void setTxCallerService(String txCallerService) {
        this.txCallerService = txCallerService;
    }

    public String getTxCaller() {
        return txCaller;
    }

    public void setTxCaller(String txCaller) {
        this.txCaller = txCaller;
    }

    public Long getTryTimeout() {
        return tryTimeout;
    }

    public void setTryTimeout(Long tryTimeout) {
        this.tryTimeout = tryTimeout;
    }

    public String getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(String isSerial) {
        this.isSerial = isSerial;
    }

    public String getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(String txStatus) {
        this.txStatus = txStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExecStatus() {
        return execStatus;
    }

    public void setExecStatus(String execStatus) {
        this.execStatus = execStatus;
    }
}
