package com.markliu.study.classload;

/**
 * author:sunnymarkliu
 * date  :16-9-18
 * time  :上午10:18
 */
public class FooClass {

    private static String bar = null;
    static {
        System.out.println("some static code execute!");
        bar = "some variable";
    }

    public FooClass() {
        System.out.println("constructor:FooClass() execute!");
    }

    public static void fun() {
        System.out.println("bar:" + bar);
    }
}
