package com.xyz.ms.service.userservice.service;

import com.xyz.base.dbutil.ResultBean;
import com.xyz.base.dbutil.TuserUser;
import com.xyz.ms.service.userservice.client.AuthClientService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AuthClientService authClientService;

    private List<String> noAuthList = Arrays.asList("/user/login");

    public boolean checkAuth(String accessToken, String requestUri) {

        try {
            if (noAuthList.contains(requestUri)) {
                return true;
            }

            if (StringUtils.isEmpty(accessToken)) {
                throw new Exception("access token不能为空。");
            }

            if (StringUtils.isEmpty(requestUri)) {
                throw new Exception("uri不能为空。");
            }

            ResultBean<TuserUser> rb = authClientService.getUserByToken(accessToken);
            if (!rb.isResult()) {
                throw new Exception(rb.getMessage());
            }
        } catch(Exception e) {
            return false;
        }

        return true;
    }

}
