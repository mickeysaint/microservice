package com.xyz.base.util;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

    public static String objToString(Object o) {
        return o==null?"":o.toString();
    }

    public static Long objToLong(Object o) {
        String str = objToString(o);
        return StringUtils.isEmpty(str)?null:Long.valueOf(str);
    }

}
