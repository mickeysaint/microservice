package com.xyz.ms.service.userservice.controller;

import com.xyz.base.common.Page;
import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.util.AssertUtils;
import com.xyz.base.vo.user.UserVo;
import com.xyz.ms.service.userservice.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Value("${foo}")
    private String foo;

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/foo")
    public String foo() {
        return foo;
    }

    @RequestMapping("/add")
    public ResultBean<Void> add(UserPo userPo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(userPo != null, "没有接收到用户数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(userPo.getUsername()), "用户编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(userPo.getUserFullName()), "用户姓名不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(userPo.getPassword()), "密码不能为空。");

            AssertUtils.isTrue(userService.exists(userPo.getUsername()), "该用户已经存在。");

            userPo.setPassword(new BCryptPasswordEncoder().encode(userPo.getPassword()));
            userService.save(userPo);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
        }

        return ret;
    }

    @RequestMapping("/findById")
    public ResultBean<UserPo> findById(Long id) {
        ResultBean<UserPo> ret = new ResultBean<>();
        UserPo userPo = userService.findById(id);
        ret.setData(userPo);
        return ret;
    }

    @RequestMapping("/findByUsername")
    public ResultBean<UserPo> findByUsername(String username) {
        ResultBean<UserPo> ret = new ResultBean<>();
        UserPo userPo = userService.findByUsername(username);
        ret.setData(userPo);
        return ret;
    }

    @RequestMapping("/getListData")
    public ResultBean<Page<UserVo>> getListData(@RequestBody Map params, HttpServletRequest request) {
        UserPo currentUser = userService.getCurrentUser(request);
        ResultBean<Page<UserVo>> ret = new ResultBean<>();
        Page<UserVo> users = userService.getListData(params, currentUser);
        ret.setData(users);
        return ret;
    }

    @RequestMapping("/save")
    public ResultBean<Void> save(@RequestBody UserVo userVo) {
        ResultBean<Void> ret = new ResultBean<>();
        try {
            AssertUtils.isTrue(userVo != null, "没有接收到用户数据。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(userVo.getUsername()), "用户编码不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(userVo.getUserFullName()), "用户姓名不能为空。");
            AssertUtils.isTrue(StringUtils.isNotEmpty(userVo.getPassword()), "密码不能为空。");

            AssertUtils.isTrue(!userService.exists(userVo.getUsername()), "该用户已经存在。");

            userService.saveUser(userVo);
        } catch(BusinessException e) {
            ret.setSuccess(false);
            ret.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } catch(Exception e) {
            ret.setSuccess(false);
            ret.setMessage("操作失败");
            logger.error("操作失败", e);
        }

        return ret;
    }
}
