package net.dreamlu.easy.commons.searcher;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.jfinal.log.Log;

import net.dreamlu.easy.commons.utils.ClassUtils;

/**
 * 类查找器，来自jetbrick-template-1x
 * @author Dreamlu
 */
public class ClassSearcher {
    private static final Log log = Log.getLog(ClassSearcher.class);
    
    public static Set<Class<?>> getClasses(Class<? extends Annotation>[] annotations, boolean skiperrors) {
        return getClasses((String[]) null, true, annotations, skiperrors);
    }

    public static Set<Class<?>> getClasses(List<String> packageNames, boolean recursive, Class<? extends Annotation>[] annotations, boolean skiperrors) {
        String[] pkgs = packageNames.toArray(new String[packageNames.size()]);
        return getClasses(pkgs, recursive, annotations, skiperrors);
    }

    public static Set<Class<?>> getClasses(String[] packageNames, boolean recursive, Class<? extends Annotation>[] annotations, final boolean skiperrors) {
        final AnnotationClassReader reader = new AnnotationClassReader();
        for (Class<? extends Annotation> annotation : annotations) {
            reader.addAnnotation(annotation);
        }

        final Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        final ClassLoader loader = ClassUtils.getClassLoader();
        
        FileSearcher finder = new FileSearcher() {
            @Override
            public void visitFileEntry(FileEntry file) {
                try {
                    if (file.isJavaClass()) {
                        if (reader.isAnnotationed(file.getInputStream())) {
                            addClass(file.getQualifiedJavaName());
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            private void addClass(String qualifiedClassName) {
                try {
                    Class<?> klass = loader.loadClass(qualifiedClassName);
                    classes.add(klass);
                } catch (ClassNotFoundException e) {
                } catch (Throwable e) {
                    if (skiperrors) {
                        log.warn("Class load error.", e);
                    } else {
                        if (e instanceof RuntimeException) {
                            throw (RuntimeException) e;
                        } else if (e instanceof Error) {
                            throw (Error) e;
                        } else {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };

        finder.lookupClasspath(packageNames, recursive);

        return classes;
    }
}
