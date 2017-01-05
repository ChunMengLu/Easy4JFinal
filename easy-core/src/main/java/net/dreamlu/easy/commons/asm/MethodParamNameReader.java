package net.dreamlu.easy.commons.asm;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * 方法参数名读取
 * 参考：
 * org.springframework.core.LocalVariableTableParameterNameDiscoverer.java
 * com.blade.kit.AsmKit.java
 * 
 * @author L.cm
 *
 */
public class MethodParamNameReader {

	/**
	 * 读取方法得参数名
	 * @param method 方法
	 * @return 方法参数名
	 */
	public static String[] read(Method method) {
		final Type[] types = Type.getArgumentTypes(method);
		final String[] paramNames = new String[types.length];
		final String n = method.getDeclaringClass().getName();
		ClassReader cr = null;
		try {
			cr = new ClassReader(n);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		cr.accept(new ClassVisitor(Opcodes.ASM5) {
			@Override
			public MethodVisitor visitMethod(final int access, final String name, final String desc,
					final String signature, final String[] exceptions) {
				final Type[] args = Type.getArgumentTypes(desc);
				// 方法名相同并且参数类型个数完全相同
				if (!name.equals(method.getName()) || !Arrays.equals(args, types)) {
					return super.visitMethod(access, name, desc, signature, exceptions);
				}
				MethodVisitor v = super.visitMethod(access, name, desc, signature, exceptions);
				return new MethodVisitor(Opcodes.ASM5, v) {
					@Override
					public void visitLocalVariable(String name, String desc, String signature, Label start, Label end,
							int index) {
						int i = index - 1;
						// 如果是静态方法，则第一就是参数
						// 如果不是静态方法，则第一个是"this"，然后才是方法的参数
						if ((access & Opcodes.ACC_STATIC) > 0) {
							i = index;
						}
						if (i >= 0 && i < paramNames.length) {
							paramNames[i] = name;
						}
						super.visitLocalVariable(name, desc, signature, start, end, index);
					}
				};
			}
		}, ClassReader.SKIP_FRAMES);
		return paramNames;
	}

}
