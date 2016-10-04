package com.markliu.tiny4j.util;

import com.markliu.tiny4j.ioc.AnnotationClassUtil;
import org.junit.Test;
import java.util.Set;

/**
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :下午4:08
 */
public class TestAnnotationClassUtil {

    @Test
    public void testGetAnnotationClass() {
        Set<Class<?>> classSet = AnnotationClassUtil.getAnnotationClassSet();
        for (Class<?> cls : classSet) {
            System.out.println(cls.getName());
        }
    }
}
