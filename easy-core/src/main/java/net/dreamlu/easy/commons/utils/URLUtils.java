package net.dreamlu.easy.commons.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL相关工具类
 */
public class URLUtils {
	/**
	 * URL编码
	 * @param url 地址
	 * @return {String}
	 */
	public static String encode(String url) {
		try {
			return URLEncoder.encode(url, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * URL解码
	 * @param url 地址
	 * @return {String}
	 */
	public static String decode(String url) {
		try {
			return URLDecoder.decode(url, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
