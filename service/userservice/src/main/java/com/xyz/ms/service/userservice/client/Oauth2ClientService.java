package com.xyz.ms.service.userservice.client;

import com.xyz.base.common.ResultBean;
import com.xyz.base.po.user.UserPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="service-oauth2")
public interface Oauth2ClientService {
    @RequestMapping(method= RequestMethod.GET, value="/oauth2/getUserByToken")
    public ResultBean<UserPo> getUserByToken(@RequestParam("accessToken") String accessToken);

}
