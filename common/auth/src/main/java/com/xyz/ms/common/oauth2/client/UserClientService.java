package com.xyz.ms.common.oauth2.client;

import com.xyz.base.common.ResultBean;
import com.xyz.base.po.user.UserPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="service-user")
public interface UserClientService {

    @RequestMapping(method= RequestMethod.GET, value="/user/findByUsername")
    public ResultBean<UserPo> findByUsername(@RequestParam("username") String username);

}
