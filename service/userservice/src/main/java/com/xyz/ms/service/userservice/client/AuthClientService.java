package com.xyz.ms.service.userservice.client;

import com.xyz.base.dbutil.ResultBean;
import com.xyz.base.dbutil.TuserUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-auth")
public interface AuthClientService {

    @RequestMapping(method= RequestMethod.GET, value="/user/getUserByToken")
    public ResultBean<TuserUser> getUserByToken(@RequestParam("access_token") String access_token);

}
