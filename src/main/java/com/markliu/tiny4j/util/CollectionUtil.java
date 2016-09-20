package com.markliu.tiny4j.util;

import java.util.Collection;

/**
 * 集合工具类
 * <p>
 * author:sunnymarkliu
 * date  :16-9-20
 * time  :上午11:04
 */
public class CollectionUtil {

    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }
}
