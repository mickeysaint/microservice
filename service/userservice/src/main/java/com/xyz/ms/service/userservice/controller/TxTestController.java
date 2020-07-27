package com.xyz.ms.service.userservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.xyz.base.common.ResultBean;
import com.xyz.base.tx.TxRegisterDto;
import com.xyz.ms.service.userservice.client.TxClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/tx")
public class TxTestController {

    private static int amount = 1000;

    private static Map<String, Object> lockMap = new HashMap<String, Object>();

    @Autowired
    private TxClientService txClientService;

    Logger logger = LoggerFactory.getLogger(TxTestController.class);

    // check and lock
    @RequestMapping("/doTry")
    public ResultBean<Void> doTry(@RequestBody JSONObject jo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            String txId = jo.getString("txId");
            if (lockMap.get(txId) != null) {
                return ret;
            }

            logger.info("try--操作前金额("+amount+")，txId=" + txId);

            Integer decAmount = jo.getInteger("dec_amount");
            Assert.isTrue(decAmount>0, "操作金额必须大于0");
            amount = amount-decAmount;

            if (amount < 0) {
                throw new Exception("金额不足");
            }

            lockMap.put(txId, decAmount);

            logger.info("try--操作后金额("+amount+")，txId=" + txId);
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
            logger.error("操作失败", e);
        }

        return ret;
    }

    @RequestMapping("/doConfirm")
    public ResultBean<Void> doConfirm(@RequestBody JSONObject jo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            String txId = jo.getString("txId");
            if (lockMap.get(txId) != null) {
                String f = "confirm--扣除金额(%s), 操作后金额(%s)，txId=%s";
                logger.info(String.format(f,lockMap.get(txId), amount, txId));
                lockMap.remove(txId);
            }
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
            logger.error("操作失败", e);
        }

        return ret;
    }

    @RequestMapping("/doCancel")
    public ResultBean<Void> doCancel(@RequestBody JSONObject jo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            String txId = jo.getString("txId");
            if (lockMap.get(txId) != null) {
                Integer lockedAmount = (Integer)lockMap.get(txId);
                amount = amount + lockedAmount;
                String f = "cancel--回滚金额(%s), 操作后金额(%s)，txId=%s";
                logger.info(String.format(f,lockMap.get(txId), amount, txId));
                lockMap.remove(txId);
            }
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
            logger.error("操作失败", e);
        }

        return ret;
    }

    @RequestMapping("/testTx")
    public ResultBean<Void> testTx(@RequestBody JSONObject jo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            String baseServiceUrlPrefix = "http://127.0.0.1:18000";
            String userServiceUrlPrefix = "http://127.0.0.1:18001";

            TxRegisterDto txRegisterDto = new TxRegisterDto();
            txRegisterDto.setTxCode("test");
            txRegisterDto.setTxName("test");
            txRegisterDto.setTxCaller("smz");
            txRegisterDto.setTxCallerService("service-user");
            TxRegisterDto.TxRegisterDto_CalleeDto uCalleeDto = new TxRegisterDto.TxRegisterDto_CalleeDto();
            uCalleeDto.setTxUrlTry(userServiceUrlPrefix + "/tx/doTry");
            uCalleeDto.setParamsTry("{\"dec_amount\":100}");
            uCalleeDto.setTxUrlConfirm(userServiceUrlPrefix + "/tx/doConfirm");
            uCalleeDto.setParamsConfirm("{}");
            uCalleeDto.setTxUrlCancel(userServiceUrlPrefix + "/tx/doCancel");
            uCalleeDto.setParamsCancel("{}");
            TxRegisterDto.TxRegisterDto_CalleeDto bCalleeDto = new TxRegisterDto.TxRegisterDto_CalleeDto();
            bCalleeDto.setTxUrlTry(baseServiceUrlPrefix + "/tx/doTry");
            bCalleeDto.setParamsTry("{\"inc_amount\":100}");
            bCalleeDto.setTxUrlConfirm(baseServiceUrlPrefix + "/tx/doConfirm");
            bCalleeDto.setParamsConfirm("{}");
            bCalleeDto.setTxUrlCancel(baseServiceUrlPrefix + "/tx/doCancel");
            bCalleeDto.setParamsCancel("{}");
            txRegisterDto.setTxCalleeData(Arrays.asList(uCalleeDto, bCalleeDto));
            txClientService.register(txRegisterDto);
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
            logger.error("操作失败", e);
        }

        return ret;
    }

}
