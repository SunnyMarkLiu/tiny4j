package com.markliu.tiny4j.ioc;

import com.markliu.tiny4j.config.ConfigHelper;
import com.markliu.tiny4j.util.ArrayUtil;
import com.markliu.tiny4j.util.AssertUtil;
import com.markliu.tiny4j.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类加载扫描工具类
 * <p>
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :下午12:37
 */
public class ClassScanLoadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassScanLoadUtil.class);

    /**
     * 获取当前线程的类加载器
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 根据类名加载该类
     *
     * @param className  类的全类名
     * @param initialize 是否初始化，此处指的是是否执行静态代码块
     * @return Class
     */
    public static Class<?> loadClass(String className, boolean initialize) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className, initialize, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class" + className + " error !", e);
            throw new RuntimeException(e);
        }
        return clazz;
    }

    /**
     * 获取所有的 class 集合
     */
    public static Set<Class<?>> getAllClassSet() {
        String appBasePackage = ConfigHelper.getAppBasePackage();
        return scanPackageClasses(appBasePackage);
    }

    /**
     * 扫描指定包下所包含的所有类及其子类
     *
     * @param packageName 所要扫描的包名，例如:com.foo.bar
     * @return Set<Class>
     */
    public static Set<Class<?>> scanPackageClasses(String packageName) {

        Set<Class<?>> classSet = new HashSet<Class<?>>();
        // 参数检查
        AssertUtil.notNull(packageName, "packageName can not be null!");

        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {// 如果该包对应的是文件夹类型，即是手动创建的包。需要递归扫描
                    String packagePath = url.getPath().replaceAll("%20", " ");
                    // 递归扫描包下的类及其子类
                    doRecursionScan(classSet, packagePath, packageName);
                } else if ("jar".equals(protocol)) { // 该包对应的是 jar 包
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if (jarURLConnection != null) {
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> entries = jarFile.entries();
                            while (entries.hasMoreElements()) {
                                JarEntry jarEntry = entries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if (jarEntryName.endsWith(".class")) {
                                    // jarEntryName : org/apache/commons/lang3/builder/CompareToBuilder.class
                                    // 类的全类名
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                    // 添加扫描到的类Class<?>到集合中
                                    addScanedClass(classSet, className);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("scan package classes failure!", e);
            throw new RuntimeException(e);
        }

        return classSet;
    }

    /**
     * 递归扫描指定包路径下的类及其子类
     *
     * @param classSet    扫描到的类集合
     * @param packagePath 包路径
     */
    private static void doRecursionScan(Set<Class<?>> classSet, String packagePath, String packageName) {

        // 列出包下的所有文件及文件夹
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });

        if (ArrayUtil.isEmpty(files))
            return;

        // 遍历 files，对于 class 文件和目录分别处理
        for (File file : files) {
            String className = file.getName();
            if (StringUtil.isNotEmpty(className)) {
                className = packageName + "." + className;
            }
            if (file.isFile()) {
                className = className.substring(0, className.lastIndexOf("."));
                addScanedClass(classSet, className);
            } else {
                String path = file.getPath();
                doRecursionScan(classSet, path, className);
            }
        }
    }

    /**
     * 加载 className 指定的类，添加进扫描到的类集合中
     *
     * @param classSet  扫描到的类集合
     * @param className String类型的包名
     */
    private static void addScanedClass(Set<Class<?>> classSet, String className) {
        // 获取 String 包类型所对应的 Class 对象，即加载该 Class
        Class<?> cls = loadClass(className, false);// 加载 Class，不执行静态代码块
        classSet.add(cls);
    }
}
