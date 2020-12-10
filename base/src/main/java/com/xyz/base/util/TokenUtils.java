package com.xyz.base.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class TokenUtils {

    private static final String TOKEN_NAME = "accessToken";

    public static String getToken(HttpServletRequest request) {
        String accessToken = null;
        accessToken = getTokenFromCookie(request);
        if (accessToken == null) {
            accessToken = getTokenFromHeader(request);
        }
        return accessToken;
    }

    private static String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(TOKEN_NAME);
    }

    private static String getTokenFromCookie(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), TOKEN_NAME)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        return token;
    }

}
