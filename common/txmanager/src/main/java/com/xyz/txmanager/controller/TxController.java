package com.xyz.txmanager.controller;

import com.xyz.base.common.ResultBean;
import com.xyz.base.tx.TxRegisterDto;
import com.xyz.base.tx.TxRegisterReturnDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tx")
public class TxController {

    @RequestMapping("/register")
    public ResultBean<TxRegisterReturnDto> register(TxRegisterDto dto) {
        ResultBean<TxRegisterReturnDto> ret = new ResultBean<>();
//        TxRegisterReturnDto dictType = tbaseDictTypeService.findById(id);
//        ret.setData(dictType);
        return ret;
    }
}
