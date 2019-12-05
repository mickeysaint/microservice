package com.xyz.ms.service.userservice.controller;

import com.xyz.base.common.ResultBean;
import com.xyz.base.exception.BusinessException;
import com.xyz.base.po.user.UserPo;
import com.xyz.base.util.AssertUtils;
import com.xyz.ms.service.userservice.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
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
}
