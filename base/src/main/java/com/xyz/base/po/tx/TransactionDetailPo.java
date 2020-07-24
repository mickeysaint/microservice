package com.xyz.base.po.tx;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xyz.base.po.BasePo;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name="tx_transaction_detail")
public class TransactionDetailPo extends BasePo {
    @Column(name="ID")
    private Long id;

    @Column(name="TX_ID")
    private String txId;

    @Column(name="TX_URL_TRY")
    private String txUrlTry;

    @Column(name="PARAMS_TRY")
    private String paramsTry;

    @Column(name="TX_URL_CONFIRM")
    private String txUrlConfirm;

    @Column(name="PARAMS_CONFIRM")
    private String paramsConfirm;

    @Column(name="TX_URL_CANCEL")
    private String txUrlCancel;

    @Column(name="PARAMS_CANCEL")
    private String paramsCancel;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="TRY_TIME")
    private Date tryTime;

    @Column(name="TRY_RESULT")
    private String tryResult;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="CONFIRM_TIME")
    private Date confirmTime;

    @Column(name="CONFIRM_RESULT")
    private String confirmResult;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="CANCEL_TIME")
    private Date cancelTime;

    @Column(name="CANCEL_RESULT")
    private String cancelResult;

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

    public Date getTryTime() {
        return tryTime;
    }

    public void setTryTime(Date tryTime) {
        this.tryTime = tryTime;
    }

    public String getTryResult() {
        return tryResult;
    }

    public void setTryResult(String tryResult) {
        this.tryResult = tryResult;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getConfirmResult() {
        return confirmResult;
    }

    public void setConfirmResult(String confirmResult) {
        this.confirmResult = confirmResult;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelResult() {
        return cancelResult;
    }

    public void setCancelResult(String cancelResult) {
        this.cancelResult = cancelResult;
    }
}
