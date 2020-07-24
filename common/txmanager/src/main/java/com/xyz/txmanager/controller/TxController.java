package com.xyz.txmanager.controller;

import com.xyz.base.common.ResultBean;
import com.xyz.base.po.tx.TransactionPo;
import com.xyz.base.tx.TxRegisterDto;
import com.xyz.base.tx.TxRegisterReturnDto;
import com.xyz.base.tx.TxStatusDto;
import com.xyz.txmanager.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tx")
public class TxController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/register")
    public ResultBean<TxRegisterReturnDto> register(@RequestBody TxRegisterDto dto) {
        ResultBean<TxRegisterReturnDto> ret = new ResultBean<>();

        try {
            transactionService.register(dto);
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        }

        return ret;
    }

    @RequestMapping("/status")
    public ResultBean<TxStatusDto> status(String txId) {
        ResultBean<TxStatusDto> ret = new ResultBean<>();

        try {
            Assert.hasText(txId, "事务ID不能为空。");
            TransactionPo transactionPo = transactionService.findByTxId(txId);
            Assert.notNull(transactionPo, "找不到数据");

            TxStatusDto dto = new TxStatusDto();
            dto.setTxId(txId);
            dto.setMessage(transactionPo.getMessage());
            dto.setTxStatus(transactionPo.getTxStatus());
            ret.setData(dto);
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        }

        return ret;
    }
}
