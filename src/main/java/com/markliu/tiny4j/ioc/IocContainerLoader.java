package com.markliu.tiny4j.ioc;

import com.markliu.tiny4j.aop.AopHelper;

/**
 * 初始化加载 Ioc 容器
 * <p>
 * author:sunnymarkliu
 * date  :16-9-20
 * time  :下午1:04
 */
public class IocContainerLoader {

    public static void initIocContainer() {
        Class<?>[] loadClass = {AopHelper.class, IoCInjectHelper.class};
        for (Class<?> cls : loadClass) {
            ClassScanLoadUtil.loadClass(cls.getName(), true);
        }
    }
}
