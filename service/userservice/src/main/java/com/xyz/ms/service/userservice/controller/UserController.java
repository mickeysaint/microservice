package com.xyz.ms.service.userservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xyz.base.dbutil.ResultBean;
import com.xyz.base.dbutil.TuserUser;
import com.xyz.base.util.AssertUtils;
import com.xyz.base.util.BusinessException;
import com.xyz.ms.service.userservice.client.AuthClientService;
import com.xyz.ms.service.userservice.service.UserService;
import feign.Feign;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Value("${foo}")
    private String foo;

    @Autowired
    private UserService userService;

    @RequestMapping("/foo")
    public String foo() {
        return foo;
    }

    @RequestMapping("/add")
    public ResultBean<Void> add(TuserUser user) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(user != null, "没有接收到用户数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(user.getUserCode()), "用户编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(user.getUserName()), "用户姓名不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(user.getPassword()), "密码不能为空。");

            AssertUtils.isTrue(userService.exists(user.getUserCode()), "该用户已经存在。");

            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userService.save(user);
        } catch(BusinessException e) {
            ret.setResult(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setResult(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/getById")
    public ResultBean<TuserUser> getUser(Long id) {
        ResultBean<TuserUser> ret = new ResultBean<>();
        TuserUser tsUser = userService.findById(id);
        ret.setData(tsUser);
        return ret;
    }

    @RequestMapping("/getByCode")
    public ResultBean<TuserUser> getByCode(String userCode) {
        ResultBean<TuserUser> ret = new ResultBean<>();
        TuserUser tuserUser = userService.findByCode(userCode);
        ret.setData(tuserUser);
        return ret;
    }

    @RequestMapping("/login")
    public ResultBean<TuserUser> login(String userCode, String password) {
        ResultBean<TuserUser> ret = new ResultBean<>();
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(
                    "http://localhost:9999/oauth/token?grant_type=password&username=" + userCode + "&password=" + password);
            httpGet.addHeader("Authorization", "Basic cGFzc3dvcmRfY2xpZW50OjEyMzQ1Ng==");
            HttpResponse resp = httpClient.execute(httpGet);
            String retJson = EntityUtils.toString(resp.getEntity());
            JSONObject jo=JSON.parseObject(retJson);
            TuserUser user = new TuserUser();
            user.setUserCode(userCode);
            user.setPassword(password);
            user.setAccessToken(jo.getString("access_token"));
            ret.setData(user);
        } catch(Exception e) {
            ret.setResult(false);
            ret.setMessage(e.getMessage());
        }
        return ret;
    }
}
