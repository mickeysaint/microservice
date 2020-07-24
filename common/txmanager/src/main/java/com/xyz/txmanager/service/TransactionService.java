package com.xyz.txmanager.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.tx.TransactionDetailPo;
import com.xyz.base.po.tx.TransactionPo;
import com.xyz.base.service.BaseService;
import com.xyz.base.tx.TxRegisterDto;
import com.xyz.txmanager.dao.TransactionDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class TransactionService extends BaseService<TransactionPo>  {

    Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public static final String STATUS_INIT = "init";
    public static final String STATUS_TRY_OK = "try_ok";
    public static final String STATUS_TRY_FAILED = "try_failed";
    public static final String STATUS_CONFIRM_OK = "confirm_ok";
    public static final String STATUS_CANCEL_OK = "cancel_ok";

    public static final String EXEC_STATUS_INIT = "init";
    public static final String EXEC_STATUS_RUNNING = "running";
    public static final String EXEC_STATUS_COMPLETE = "complete";
    public static final String EXEC_STATUS_EXCEPTION = "exception";

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private TransactionDetailService transactionDetailService;

    private ThreadPoolExecutor threadPool;

    @Autowired
    public void setDao(TransactionDao dao) {
        this.dao = dao;
    }

    public TransactionPo findByTxId(String txId) {
        TransactionPo eg = new TransactionPo();
        eg.setTxId(txId);
        return findOneByEg(eg);
    }

    @Transactional
    public void register(TxRegisterDto dto) {
        TransactionPo transactionPo = new TransactionPo();
        transactionPo.setTxId(UUID.randomUUID().toString());
        transactionPo.setTxCode(dto.getTxCode());
        transactionPo.setTxName(dto.getTxName());
        transactionPo.setTxCallerService(dto.getTxCallerService());
        transactionPo.setTxCaller(dto.getTxCaller());
        transactionPo.setCreateTime(new Date());
        transactionPo.setTxStatus(STATUS_INIT);
        transactionPo.setExecStatus(EXEC_STATUS_INIT);
        transactionDao.save(transactionPo);

        List<TxRegisterDto.TxRegisterDto_CalleeDto> calleeList = dto.getTxCalleeData();
        Assert.notEmpty(calleeList, "被调用者数据不能为空。");
        for (TxRegisterDto.TxRegisterDto_CalleeDto calleeDto : calleeList) {
            TransactionDetailPo transactionDetailPo = new TransactionDetailPo();
            transactionDetailPo.setTxId(transactionPo.getTxId());
            transactionDetailPo.setTxUrlTry(calleeDto.getTxUrlTry());
            transactionDetailPo.setParamsTry(calleeDto.getParamsTry());
            transactionDetailPo.setTxUrlConfirm(calleeDto.getTxUrlConfirm());
            transactionDetailPo.setParamsConfirm(calleeDto.getParamsConfirm());
            transactionDetailPo.setTxUrlCancel(calleeDto.getTxUrlCancel());
            transactionDetailPo.setParamsCancel(calleeDto.getParamsCancel());
            transactionDetailService.save(transactionDetailPo);
        }

        logger.info("add transaction to thread pool, txId=" + transactionPo.getTxId());

        String msgFormat = "pool size:%s, task count:%s, active count:%s";
        logger.info(String.format(msgFormat, threadPool.getPoolSize(),
                threadPool.getTaskCount(), threadPool.getActiveCount()));

        threadPool.execute(() -> {
            this.execTransaction(transactionPo.getTxId());
        });
    }

    public void initThreadPool() {
        threadPool = (ThreadPoolExecutor)Executors.newCachedThreadPool();
    }

    public void execTransaction(String txId) {
        // 获取事务数据
        logger.info("获取事务数据， txId=" + txId);
        TransactionPo transactionPo = findByTxId(txId);
        List<TransactionDetailPo> transactionDetailPoList = transactionDetailService.findListByTxId(txId);

        transactionPo.setExecStatus(EXEC_STATUS_RUNNING);
        transactionPo.setUpdateTime(new Date());
        this.update(transactionPo);

        // 执行try
        logger.info("开始执行try， txId=" + txId);
        String txStatus = STATUS_TRY_OK;
        for (TransactionDetailPo transactionDetailPo : transactionDetailPoList) {
            try {
                execTransactionDetailTry(transactionDetailPo);
                transactionDetailPo.setTryResult("Y");
                transactionDetailPo.setTryTime(new Date());
                transactionDetailService.update(transactionDetailPo);
            } catch(Exception e) {
                logger.error(transactionDetailPo.toString());
                logger.error("执行try失败", e);
                transactionDetailPo.setTryResult("N");
                transactionDetailPo.setTryTime(new Date());
                transactionDetailService.update(transactionDetailPo);
                txStatus = STATUS_TRY_FAILED;
                transactionPo.setTxStatus(txStatus);
                transactionPo.setUpdateTime(new Date());
                this.update(transactionPo);
                break;
            }
        }
        logger.info("执行try完成，结果为" + txStatus + ", txId=" + txId);

        // 如果try_ok，执行confirm，否则执行cancel
        boolean hasException = false;
        if (StringUtils.equals(txStatus, STATUS_TRY_OK)) {
            logger.info("开始执行confirm， txId=" + txId);

            for (TransactionDetailPo transactionDetailPo : transactionDetailPoList) {
                try {
                    execTransactionDetailConfirm(transactionDetailPo);
                    transactionDetailPo.setConfirmResult("Y");
                    transactionDetailPo.setConfirmTime(new Date());
                    transactionDetailService.update(transactionDetailPo);
                } catch(Exception e) {
                    logger.error(transactionDetailPo.toString());
                    logger.error("执行confirm失败", e);
                    transactionDetailPo.setConfirmResult("N");
                    transactionDetailPo.setConfirmTime(new Date());
                    transactionDetailService.update(transactionDetailPo);
                    transactionPo.setExecStatus("exception");
                    transactionPo.setUpdateTime(new Date());
                    this.update(transactionPo);
                    hasException = true;
                }
            }
            txStatus = STATUS_CONFIRM_OK;

            logger.info("执行confirm完成");
        } else {
            logger.info("开始执行cancel， txId=" + txId);

            for (TransactionDetailPo transactionDetailPo : transactionDetailPoList) {
                if (transactionDetailPo.getTryTime() != null) {
                    try {
                        execTransactionDetailCancel(transactionDetailPo);
                        transactionDetailPo.setCancelResult("Y");
                        transactionDetailPo.setCancelTime(new Date());
                        transactionDetailService.update(transactionDetailPo);
                    } catch (Exception e) {
                        logger.error(transactionDetailPo.toString());
                        logger.error("执行cancel失败", e);
                        transactionDetailPo.setCancelResult("N");
                        transactionDetailPo.setCancelTime(new Date());
                        transactionDetailService.update(transactionDetailPo);
                        transactionPo.setExecStatus("exception");
                        transactionPo.setUpdateTime(new Date());
                        this.update(transactionPo);
                        hasException = true;
                    }
                }
            }
            txStatus = STATUS_CANCEL_OK;

            logger.info("执行cancel完成");
        }

        if (!hasException) {
            transactionPo.setTxStatus(txStatus);
            transactionPo.setExecStatus(EXEC_STATUS_COMPLETE);
            transactionPo.setUpdateTime(new Date());
            this.update(transactionPo);
        } else {
            transactionPo.setExecStatus(EXEC_STATUS_EXCEPTION);
            transactionPo.setUpdateTime(new Date());
            this.update(transactionPo);
        }
    }

    private void execTransactionDetailTry(TransactionDetailPo transactionDetailPo) {
        String url = transactionDetailPo.getTxUrlTry();
        String params = transactionDetailPo.getParamsTry();
        String result = execHttpCall(url, params);
        JSONObject joRet = JSON.parseObject(result);
        boolean success = joRet.getBoolean("success");
        if (!success) {
            throw new BusinessException(joRet.getString("message"));
        }
    }

    private void execTransactionDetailConfirm(TransactionDetailPo transactionDetailPo) {
        String url = transactionDetailPo.getTxUrlConfirm();
        String params = transactionDetailPo.getParamsConfirm();
        String result = execHttpCall(url, params);
        JSONObject joRet = JSON.parseObject(result);
        boolean success = joRet.getBoolean("success");
        if (!success) {
            throw new BusinessException(joRet.getString("message"));
        }
    }

    private void execTransactionDetailCancel(TransactionDetailPo transactionDetailPo) {
        String url = transactionDetailPo.getTxUrlCancel();
        String params = transactionDetailPo.getParamsCancel();
        String result = execHttpCall(url, params);
        JSONObject joRet = JSON.parseObject(result);
        boolean success = joRet.getBoolean("success");
        if (!success) {
            throw new BusinessException(joRet.getString("message"));
        }
    }

    private String execHttpCall(String url, String params) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.APPLICATION_JSON_UTF8;
        headers.setContentType(type);
        HttpEntity<String> entity = new HttpEntity<>(params, headers);
        JSONObject joRet = restTemplate.postForObject(url, entity, JSONObject.class);
        return joRet.toString();
    }
}
