package com.xyz.ms.service.userservice.filter;

import com.alibaba.fastjson.JSON;
import com.xyz.base.dbutil.ResultBean;
import com.xyz.ms.service.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.LogRecord;

@Component
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        String accessToken = req.getParameter("accessToken");
        String requestUri = req.getRequestURI();
        WebApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        AuthService authService = ac.getBean(AuthService.class);
        if (authService.checkAuth(accessToken, requestUri)) {
            filterChain.doFilter(request, response);
            return;
        } else {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json; charset=utf-8");
            ResultBean<Void> rb = new ResultBean<Void>();
            rb.setResult(false);
            rb.setMessage("权限验证失败");
            res.getWriter().write(JSON.toJSON(rb).toString());
        }
    }

    @Override
    public void destroy() {

    }
}
