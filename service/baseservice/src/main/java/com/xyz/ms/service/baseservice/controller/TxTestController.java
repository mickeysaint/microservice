package com.xyz.ms.service.baseservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.xyz.base.common.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tx")
public class TxTestController {

    private static int amount = 1000;

    private static Map<String, Object> lockMap = new HashMap<String, Object>();

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

            Integer incAmount = jo.getInteger("inc_amount");
            Assert.isTrue(incAmount>0, "操作金额必须大于0");
            lockMap.put(txId, incAmount);
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
                Integer incAmount = (Integer)lockMap.get(txId);
                String f = "confirm--增加金额(%s), 操作后金额(%s)，txId=%s";
                amount = amount + incAmount;
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

}
