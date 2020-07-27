package com.xyz.ms.service.userservice.client;

import com.xyz.base.common.ResultBean;
import com.xyz.base.tx.TxRegisterDto;
import com.xyz.base.tx.TxRegisterReturnDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="service-tx")
public interface TxClientService {
    @RequestMapping(method= {RequestMethod.POST}, value="/tx/register", consumes = "application/json")
    public ResultBean<TxRegisterReturnDto> register(@RequestBody TxRegisterDto dto);

}
