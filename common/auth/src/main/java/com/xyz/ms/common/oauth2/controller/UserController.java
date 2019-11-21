package com.xyz.ms.common.oauth2.controller;

import com.xyz.base.dbutil.ResultBean;
import com.xyz.base.dbutil.TuserUser;
import com.xyz.ms.common.oauth2.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    @Qualifier("myTokenStore")
    private TokenStore myTokenStore;

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/getUserByToken")
    public ResultBean<TuserUser> getUserByToken(String access_token) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>" + access_token);
        ResultBean<TuserUser> resultBean = new ResultBean<>();
        OAuth2Authentication aa = myTokenStore.readAuthentication(access_token);
        if (aa == null) {
            resultBean.setResult(false);
            resultBean.setMessage("无效的token");
            return resultBean;
        }

        TuserUser eg = new TuserUser();
        eg.setUserCode(aa.getName());
        List<TuserUser> list = userService.findByEg(eg);
        if (list == null || list.size() == 0) {
            resultBean.setResult(false);
            resultBean.setMessage("找不到有效的用户信息");
            return resultBean;
        }

        resultBean.setResult(true);
        resultBean.setData(list.get(0));
        return resultBean;
    }
}
