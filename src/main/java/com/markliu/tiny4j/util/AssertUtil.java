package com.markliu.tiny4j.util;

import com.markliu.tiny4j.ioc.ClassScanLoadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :下午12:52
 */
public class AssertUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassScanLoadUtil.class);

    public static void notNull(Object object, String message) {
        if (object == null) {
            LOGGER.error("illegal argument! " + message);
            throw new IllegalArgumentException(message);
        }
    }
}
