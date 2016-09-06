package net.dreamlu.easy.commons.utils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.jfinal.kit.PathKit;

/**
 * 字符串工具类
 * @author L.cm
 */
public class StrUtils {
	/**
	 * 清除左右空格
	 * @param str 字符串
	 * @return String
	 */
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}
	
	/**
	 * 获取UUID，去掉`-`的
	 * @return uuid
	 */
	public static String getUUID () {
		return UUID.randomUUID().toString().replace("-", "");
	}

	 /**
	 * 将字符串中特定模式的字符转换成map中对应的值
	 * 
	 * use: format("my name is ${name}, and i like ${like}!", {"name":"L.cm", "like": "Java"})
	 * 
	 * @param s		需要转换的字符串
	 * @param map	转换所需的键值对集合
	 * @return		转换后的字符串
	 */
	public static String format(String s, Map<String, String> map) {
		StringBuilder sb = new StringBuilder((int)(s.length() * 1.5));
		int cursor = 0;
		for (int start, end; (start = s.indexOf("${", cursor)) != -1 && (end = s.indexOf('}', start)) != -1;) {
			sb.append(s.substring(cursor, start));
			String key = s.substring(start + 2, end);
			sb.append(map.get(StrUtils.trim(key)));
			cursor = end + 1;
		}
		sb.append(s.substring(cursor, s.length()));
		return sb.toString();
	}

	/**
	 * 实现简易的模板
	 * @param view
	 * @param map
	 * @return
	 */
	public static String render(String view, Map<String, String> map) {
		String viewPath = PathKit.getWebRootPath() + view;
		try {
			String html = FileUtils.readFileToString(new File(viewPath), "UTF-8");
			return format(html, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串格式化
	 * 
	 * use: format("my name is {0}, and i like {1}!", "L.cm", "java")
	 * 
	 * int long use {0,number,#}
	 * 
	 * @param s 
	 * @param args
	 * @return 转换后的字符串
	 */
	public static String format(String s, Object... args) {
		return MessageFormat.format(s, args);
	}

	/**
	 * 转义HTML用于安全过滤
	 * @param html
	 * @return
	 */
	public static String escapeHtml(String html) {
		if (html == null) return "";
		html = html.replace("<", "&lt;");
		html = html.replace(">", "&gt;");
		html = html.replace("\"", "&quot;");
		html = html.replace("\n", "</br>");
		return html;
	}

	/**
	 * 清理字符串，清理出某些不可见字符
	 * @param txt
	 * @return {String}
	 */
	public static String cleanChars(String txt) {
		return txt.replaceAll("[ 　	`·•�\\f\\t\\v]", "");
	}

	// 随机字符串
	private static final String _INT = "0123456789";
	private static final String _STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final String _ALL = _INT + _STR;

	private static final Random RANDOM = new Random();

	/**
	 * 生成的随机数类型
	 * @author L.cm
	 * @email: 596392912@qq.com
	 * @site: http://www.dreamlu.net
	 * @date 2015年4月20日下午9:15:23
	 */
	public static enum RandomType {
		INT, STRING, ALL;
	}

	/**
	 * 随机数生成
	 * @param count
	 * @return
	 */
	public static String random(int count, RandomType randomType) {
		if (count == 0) return "";
		if (count < 0) {
			throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
		}
		char[] buffer = new char[count];
		for (int i = 0; i < count; i++) {
			if (randomType.equals(RandomType.INT)) {
				buffer[i] = _INT.charAt(RANDOM.nextInt(_INT.length()));
			} else if (randomType.equals(RandomType.STRING)) {
				buffer[i] = _STR.charAt(RANDOM.nextInt(_STR.length()));
			}else {
				buffer[i] = _ALL.charAt(RANDOM.nextInt(_ALL.length()));
			}
		}
		return new String(buffer);
	}
	
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