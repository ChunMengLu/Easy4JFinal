package net.dreamlu.easy.commons.searcher;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.jfinal.log.Log;

import net.dreamlu.easy.commons.utils.ClassUtils;

/**
 * 类查找器
 * 从classpath或者jar中查找
 * 参考：jetbrick-template-1x
 * @author L.cm
 */
@SuppressWarnings("unchecked")
public class ClassSearcher {
    private static final Log log = Log.getLog(ClassSearcher.class);
    
    public static Set<Class<?>> getClasses(List<String> packageNames, Class<? extends Annotation> annotation) {
        String[] pkgs = packageNames.toArray(new String[packageNames.size()]);
        return getClasses(pkgs, annotation);
    }
    
    public static Set<Class<?>> getClasses(List<String> packageNames, Class<? extends Annotation>[] annotations) {
        String[] pkgs = packageNames.toArray(new String[packageNames.size()]);
        return getClasses(pkgs, annotations);
    }
    
    public static Set<Class<?>> getClasses(String[] packageNames, Class<? extends Annotation> annotation) {
        Class<? extends Annotation>[] annotations = new Class[]{annotation};
        return getClasses(packageNames, annotations);
    }
    
    public static Set<Class<?>> getClasses(String[] packageNames, Class<? extends Annotation>[] annotations) {
        final AnnotationClassReader reader = new AnnotationClassReader();
        for (Class<? extends Annotation> annotation : annotations) {
            reader.addAnnotation(annotation);
        }
        
        final Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        
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
                    Class<?> klass = ClassUtils.loadClass(qualifiedClassName);
                    classes.add(klass);
                } catch (Throwable e) {
                    log.warn("Class load error.", e);
                }
            }
        };
        
        finder.lookupClasspath(packageNames);
        
        return classes;
    }
}
