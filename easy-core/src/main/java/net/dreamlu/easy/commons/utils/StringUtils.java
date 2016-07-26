package net.dreamlu.easy.commons.utils;

public class StringUtils {

	/**
	 * 将数组转化成 分隔符相连的字符串
	 * @param array 数组
	 * @param separator 分隔符
	 * @return 字符串
	 */
	public static String join(Object[] array, CharSequence separator) {
		if (array == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder((int)(array.length * 1.5));
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sb.append(separator);
			}
			Object object = array[i];
			sb.append(object);
		}
		return sb.toString();
	}
	
}
