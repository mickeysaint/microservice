package com.xyz.base.util;

import com.xyz.base.exception.BusinessException;

import java.util.List;

public class AssertUtils {

    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new BusinessException(message);
        }
    }

    public static void isEmpty(List<?> list, String message) {
        if (list != null && list.size() > 0) {
            throw new BusinessException(message);
        }
    }

    public static void isNotEmpty(List<?> list, String message) {
        if (list == null || list.size() == 0) {
            throw new BusinessException(message);
        }
    }

}
