package net.dreamlu.easy.commons.utils;

import java.util.Arrays;

/**
 * Sql相关工具类
 */
public class SqlUtils {

	/**
	 * 生成sql占位符 ?,?,?
	 * @param size
	 * @return ?,?,?
	 */
	public static String sqlHolder(int size) {
		String[] paras = new String[size];
		Arrays.fill(paras, "?");
		return StrUtils.join(paras, ",");
	}

}
