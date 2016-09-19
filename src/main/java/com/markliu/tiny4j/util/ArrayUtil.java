package com.markliu.tiny4j.util;

/**
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :下午2:29
 */
public class ArrayUtil {

    /**
     * 判断数组是否为空
     * @param array 数组
     */
    public static boolean isEmpty(final Object[] array) {
        return array == null || array.length == 0;
    }
}
