package com.xyz.base.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtils {

    public static <K, V> Map<K, V> convertList2Map(List<V> list, String keyField, Class<K> keyClazz, Class<V> valClazz) {
        Map<K, V> mapping = new HashMap<K, V>();

        if (list != null && list.size() > 0) {
            for (V o : list) {
                BeanWrapper bw = new BeanWrapperImpl(o);
                Object key = bw.getPropertyValue(keyField);
                if (key != null) {
                    mapping.put((K)key, o);
                }
            }
        }

        return mapping;
    }

}
