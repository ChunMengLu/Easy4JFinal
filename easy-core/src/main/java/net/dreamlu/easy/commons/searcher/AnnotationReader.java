package net.dreamlu.easy.commons.searcher;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 采用asm读取类的注解，判断该类是否存在某注解
 * @author L.cm
 *
 */
public class AnnotationReader {
	private Map<String, Class<? extends Annotation>> annotationMap = new HashMap<String, Class<? extends Annotation>>();

	/**
	 * 添加注解并将注解拼接成注解字符串格式
	 * @param annotation 注解
	 */
	public void addAnnotation(Class<? extends Annotation> annotation) {
		annotationMap.put('L' + annotation.getName().replace('.', '/') + ';', annotation);
	}

	public boolean isAnnotationed(InputStream inputStream) {
		ClassReader classReader;
		try {
			classReader = new ClassReader(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// 结果集
		final boolean[] visitorResult = new boolean[]{ false };
		classReader.accept(new ClassVisitor(Opcodes.ASM5) {
			
			/**
			 * 查看注解：
			 * annotation注解字符串，visible是否runtime
			 */
			@Override
			public AnnotationVisitor visitAnnotation(String annotation, boolean visible) {
				if (annotationMap.containsKey(annotation)) {
					visitorResult[0] = true;
				}
				return super.visitAnnotation(annotation, visible);
			}
			
		}, ClassReader.SKIP_CODE);
		return visitorResult[0];
	}

}
