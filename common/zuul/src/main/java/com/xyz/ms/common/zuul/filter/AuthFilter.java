package com.xyz.ms.common.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import com.xyz.base.common.ResultBean;
import com.xyz.base.po.user.UserPo;
import com.xyz.ms.common.zuul.client.Oauth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class AuthFilter extends ZuulFilter {

    @Autowired
    private Oauth2ClientService oauth2ClientService;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 998;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    private List<String> noCheckUrls = Arrays.asList("/service-oauth2/oauth2/login");

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String accessToken = request.getHeader("accessToken");
        String requestUrl = request.getRequestURI();

        if (noCheckUrls.contains(requestUrl)) {
            return null;
        } else {
            ResultBean<UserPo> resultBean = oauth2ClientService.getUserByToken(accessToken);
            if (!resultBean.isSuccess()) {
                resultBean.setMessage("权限校验失败。");
                resultBean.setData(null);
                ctx.setSendZuulResponse(false); // 过滤该请求，不下发
                ctx.setResponseStatusCode(200); // 返回权限错误的代码
                ctx.getResponse().setContentType("application/json");
                ctx.getResponse().setCharacterEncoding("utf-8");
                ctx.setResponseBody(JSON.toJSONString(resultBean));
            }
        }

        return null;
    }
}
