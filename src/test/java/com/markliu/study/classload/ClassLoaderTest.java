package com.markliu.study.classload;

import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * author:sunnymarkliu
 * date  :16-9-18
 * time  :上午10:17
 */
public class ClassLoaderTest {

    private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    @Test
    public void testClassForName() throws Exception {
        /*
         forName(@org.jetbrains.annotations.NonNls String name,
                       boolean initialize,
                       ClassLoader loader)
         参数 initialize，表示加载该类时是否执行静态代码块。
         调用的是：forName0(name, initialize, loader, caller)。
         为提高加载类的性能，可将 initialize 设置为 false！
         */
        FooClass.fun();
        System.out.println("==================================");
        Class clazz = Class.forName(FooClass.class.getName(), false, Thread.currentThread().getContextClassLoader());
        System.out.println("clazz:" + clazz.getName());
        FooClass.fun();
        clazz.newInstance();
        System.out.println("==================================");

        /*
         * forName(String className)
         * 会执行静态代码块。
         * 调用的是：forName0(className, true, ClassLoader.getClassLoader(caller), caller);
         */
        clazz = Class.forName(FooClass.class.getName());
        System.out.println("clazz:" + clazz.getName());
    }

    /**
     * 测试获取指定包下的所有类，并加载
     */
    @Test
    public void testLoadClass() throws Exception {
        String packageName = "com.markliu.study";
        Enumeration<URL> resources = classLoader.getResources(packageName.replace(".", "/"));
        System.out.println(resources);

        Enumeration<URL> resources1 = classLoader.getResources("org/apache/commons/lang3");
        while (resources1.hasMoreElements()) {
            URL url = resources1.nextElement();
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            if (jarURLConnection != null) {
                JarFile jarFile = jarURLConnection.getJarFile();
                if (jarFile != null) {
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        String jarEntryName = jarEntry.getName();
                        if (jarEntryName.endsWith(".class")) {
                            // org/apache/commons/lang3/builder/CompareToBuilder.class
                            System.out.println(jarEntryName);
                            // 类的全类名
                            String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                            System.out.println(className);
                        }
                    }
                }
            }
            System.out.println("========================");
        }
    }

    @Test
    public void testListFile() throws Exception {
        String packageName = "com.markliu.study";
        Enumeration<URL> resources = classLoader.getResources(packageName.replace(".", "/"));
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            if (url != null) {
                String protocol = url.getProtocol();
                if (protocol.equals("file")) {
                    String path = url.getPath();
                    File[] files = new File(path).listFiles(new FileFilter() {
                        public boolean accept(File file) {
                            return file.isFile() && file.getName().endsWith(".class") ||
                                    file.isDirectory();
                        }
                    });

                    for (int i = 0; i < files.length; i++) {
                        File file = files[i];
                        System.out.println("file: " + file);
                    }
                }
            }
        }
    }
}
