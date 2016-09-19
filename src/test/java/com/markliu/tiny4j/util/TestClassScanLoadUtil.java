package com.markliu.tiny4j.util;

import com.markliu.tiny4j.ioc.ClassScanLoadUtil;
import org.junit.Test;

import java.util.Set;

/**
 * 测试 ClassScanLoadUtil
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :下午2:03
 */
public class TestClassScanLoadUtil {

    @Test
    public void testScanPackageClasses() {
        String packageName = "org.apache.commons.lang3";
        String packageName2 = "com.markliu.tiny4j";

        Set<Class<?>> classSet = ClassScanLoadUtil.scanPackageClasses(packageName2);
        for (Class<?> cls : classSet) {
            System.out.println(cls.getName());
        }
    }
}
