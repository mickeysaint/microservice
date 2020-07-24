package com.xyz.base.po.tx;

import com.xyz.base.po.BasePo;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="tx_transaction_type")
public class TransactionTypePo extends BasePo {

    @Column(name="ID")
    private Long id;

    @Column(name="TX_CODE")
    private String txCode;

    @Column(name="TX_NAME")
    private String txName;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
