package net.dreamlu.easy.commons.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * IOUtils
 * @author L.cm
 */
public abstract class IOUtils {
	/**
	 * closeQuietly
	 * @param closeable 自动关闭
	 */
	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}
}
