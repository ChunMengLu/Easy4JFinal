package net.dreamlu.easy.commons.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * 类工具集
 * @author L.cm
 */
public abstract class ClassUtils {
    /**
     * 确定class是否可以被加载
     * @param className 完整类名
     * @param classLoader 类加载
     * @return {boolean}
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            Class.forName(className, true, classLoader);
            return true;
        } catch (Throwable ex) {
            // Class or one of its dependencies is not present...
            return false;
        }
    }
    
    /**
     * 实例化对象
     * @param clazz 类
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 实例化对象
     * @param clazzStr 类名
     * @return 对象
     */
    public static <T> T newInstance(String clazzStr) {
        Class<?> clazz = ClassUtils.loadClass(clazzStr);
        return newInstance(clazz);
    }
    
    /**
     * 获取当前线程
     * @return 当前线程的class loader
     */
    public static ClassLoader getContextClassLoader() {
        ClassLoader classLoader = null;
        try {
          classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        return classLoader;
    }

    /**
     * 获得class loader
     * 若当前线程class loader不存在，取当前类的class loader
     * @return 类加载器
     */
    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = getContextClassLoader();
        if(classLoader == null) {
            // No thread context class loader -> use class loader of this class.
            classLoader = ClassUtils.class.getClassLoader();
            if (classLoader == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    classLoader = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return classLoader;
    }
    
    /**
     * 加载类
     * @param className 类名
     * @return Class
     */
    public static Class<?> loadClass(String className) {
        if (className == null) {
            return null;
        }
        ClassLoader classLoader = ClassUtils.getClassLoader();
        try {
            return (classLoader != null ? classLoader.loadClass(className) : Class.forName(className));
        } catch (ClassNotFoundException e) {
            int lastDotIndex = className.lastIndexOf('.');
            if (lastDotIndex != -1) {
                String innerClassName = className.substring(0, lastDotIndex) + '$' + className.substring(lastDotIndex + 1);
                try {
                    return (classLoader != null ? classLoader.loadClass(innerClassName) : Class.forName(innerClassName));
                } catch (ClassNotFoundException ex2) {
                    // Swallow - let original exception get through
                }
            }
        }
        return null;
    }
    
    public static final String EXT_CLASS_LOADER_NAME = "sun.misc.Launcher$ExtClassLoader";
    
    /**
     * 本段代码来自jetbrick-template-1x
     * 根据 classLoader 获取所有的 Classpath URLs.
     */
    public static Collection<URL> getClasspathURLs(final ClassLoader classLoader) {
        Collection<URL> urls = new LinkedHashSet<URL>(32);
        ClassLoader loader = classLoader;
        while (loader != null) {
            String klassName = loader.getClass().getName();
            if (EXT_CLASS_LOADER_NAME.equals(klassName)) {
                break;
            }
            if (loader instanceof URLClassLoader) {
                for (URL url : ((URLClassLoader) loader).getURLs()) {
                    urls.add(url);
                }
            } else if (klassName.startsWith("weblogic.utils.classloaders.")) {
                // 该死的 WebLogic，只能特殊处理
                // GenericClassLoader, FilteringClassLoader, ChangeAwareClassLoader
                try {
                    Method method = loader.getClass().getMethod("getClassPath");
                    Object result = method.invoke(loader);
                    if (result != null) {
                        String[] paths = StrUtils.split(result.toString(), File.pathSeparatorChar);
                        for (String path : paths) {
                            urls.add(URLUtils.fromFile(path));
                        }
                    }
                } catch (NoSuchMethodException e) {
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            loader = loader.getParent();
        }

        String classpath = System.getProperty("java.class.path");
        if (classpath.length() > 1) {
            String[] paths = StrUtils.split(classpath, File.pathSeparatorChar);
            for (String path : paths) {
                path = path.trim();
                if (path.length() > 0) {
                    URL url = URLUtils.fromFile(path);
                    urls.add(url);
                }
            }
        }

        // 添加包含所有的 META-INF/MANIFEST.MF 的 jar 文件
        try {
            Enumeration<URL> paths = classLoader.getResources("META-INF/MANIFEST.MF");
            while (paths.hasMoreElements()) {
                URL url = paths.nextElement();
                File file = URLUtils.toFileObject(url);
                urls.add(file.toURI().toURL());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 删除 jdk 自带的 jar
        Iterator<URL> it = urls.iterator();
        while (it.hasNext()) {
            String path = it.next().getPath();
            if (path.contains("/jre/lib/")) {
                it.remove();
            }
        }

        return urls;
    }
    
    /**
     * 根据 classLoader 获取指定 package 对应的 URLs.
     */
    public static Collection<URL> getClasspathURLs(ClassLoader classLoader, String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("PackageName must be not null.");
        }
        Collection<URL> urls = new ArrayList<URL>();
        String dirname = packageName.replace('.', '/');
        try {
            Enumeration<URL> dirs = classLoader.getResources(dirname);
            while (dirs.hasMoreElements()) {
                urls.add(dirs.nextElement());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return urls;
    }

}
