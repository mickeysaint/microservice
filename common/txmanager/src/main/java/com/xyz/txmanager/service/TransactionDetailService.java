package com.xyz.txmanager.service;

import com.xyz.base.po.tx.TransactionDetailPo;
import com.xyz.base.service.BaseService;
import com.xyz.txmanager.dao.TransactionDetailDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionDetailService extends BaseService<TransactionDetailPo>  {

    Logger logger = LoggerFactory.getLogger(TransactionDetailService.class);

    @Autowired
    private TransactionDetailDao transactionDetailDao;

    @Autowired
    public void setDao(TransactionDetailDao dao) {
        this.dao = dao;
    }

    public List<TransactionDetailPo> findListByTxId(String txId) {
        TransactionDetailPo eg = new TransactionDetailPo();
        eg.setTxId(txId);
        return findByEg(eg);
    }
}
