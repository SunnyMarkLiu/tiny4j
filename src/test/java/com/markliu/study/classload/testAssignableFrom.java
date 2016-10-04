package com.markliu.study.classload;

import org.junit.Test;

/**
 * author:sunnymarkliu
 * date  :16-10-4
 * time  :下午12:41
 */
public class testAssignableFrom {

    @Test
    public void testIsAssignableFrom() {

        // whether objects of the type cls can be assigned(赋值) to objects of this class
        // NullPointerException 继承自 RuntimeException，所以 NullPointerException 对象可以赋值到 RuntimeException
        NullPointerException e = new NullPointerException();
        System.out.println((RuntimeException)e);
        System.out.println(RuntimeException.class.isAssignableFrom(NullPointerException.class));
        System.out.println(NullPointerException.class.isAssignableFrom(RuntimeException.class));
    }
}
