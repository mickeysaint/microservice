package com.xyz.ms.common.oauth2.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.user.UserPo;
import com.xyz.ms.common.oauth2.bean.UserDetailsImpl;
import com.xyz.ms.common.oauth2.client.UserClientService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    @Qualifier("myTokenStore")
    private TokenStore myTokenStore;

    @Autowired
    private UserClientService userClientService;

    Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);

    private static HttpClient httpClient = null;
    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        httpClient = HttpClients.custom().setConnectionManager(cm).build();
    }

    @RequestMapping(value = "/getUserByToken")
    public ResultBean<UserPo> getUserByToken(String accessToken) {
        logger.info("get user by token, token=" + accessToken);
        ResultBean<UserPo> resultBean = new ResultBean<>();
        OAuth2Authentication oAuth2Authentication = myTokenStore.readAuthentication(accessToken);
        if (oAuth2Authentication != null) {
            UserDetails userDetails = (UserDetails)oAuth2Authentication.getPrincipal();
            if (userDetails != null) {
                ResultBean<UserPo> userRet = userClientService.findByUsername(userDetails.getUsername());
                resultBean.setData(userRet.getData());
            } else {
                resultBean.setSuccess(false);
                resultBean.setMessage("根据token获取用户信息失败。");
            }
        } else {
            resultBean.setSuccess(false);
            resultBean.setMessage("根据token获取用户信息失败。");
        }

        return resultBean;

    }

    @RequestMapping("/login")
    public ResultBean<UserPo> login(@RequestBody UserPo user) {
        String username = user.getUsername();
        String password = user.getPassword();
        logger.info("login username=" + username);

        ResultBean<UserPo> resultBean = new ResultBean<>();

        try {
            logger.info("获取用户信息。。。。");
            ResultBean<UserPo> rt = userClientService.findByUsername(username);
            logger.info("获取用户信息，返回：" + rt.toString());

            UserPo userPo = null;
            if (!rt.isSuccess()) {
                throw new BusinessException("用户不存在。");
            } else {
                userPo = rt.getData();
            }

//            String url = "http://localhost:9999/oauth/token?grant_type=password&username="
//                    + username + "&password=" + password;
            String url = "http://localhost:8040/service-oauth2/oauth/token?grant_type=password&username="
                    + username + "&password=" + password;
            logger.info("开始登录：" + url);
            HttpGet httpGet = new HttpGet(url);
            String base64Str = Base64.getEncoder().encodeToString("password_client:123456".getBytes());
            httpGet.addHeader("Authorization", "Basic " + base64Str);
            HttpResponse resp = httpClient.execute(httpGet);
            String retJson = EntityUtils.toString(resp.getEntity());
            logger.info("登录返回：" + retJson);
            JSONObject retJo= JSON.parseObject(retJson);
            String accessToken = retJo.getString("access_token");

            if (StringUtils.isEmpty(accessToken)) {
                throw new BusinessException("用户密码错误。");
            } else {
                userPo.setAccessToken(accessToken);
            }

            resultBean.setData(userPo);
        } catch(BusinessException e) {
            logger.error("登录失败", e);
            resultBean.setSuccess(false);
            resultBean.setMessage(e.getMessage());
        } catch(Exception e) {
            logger.error("登录失败", e);
            resultBean.setSuccess(false);
            resultBean.setMessage("登录失败");
        }

        return resultBean;

    }
}
