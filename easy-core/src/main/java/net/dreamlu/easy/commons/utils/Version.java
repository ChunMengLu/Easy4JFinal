package net.dreamlu.easy.commons.utils;

/**
 * 版本号比较工具
 * 
 * 思路来源于： <url>https://github.com/hotoo/versioning/blob/master/versioning.js</url>
 * 
 * example
 * <pre>
 * ##完整模式
 * Version.of("v0.1.1").eq("v0.1.2"); // false
 * 
 * ##不完整模式
 * Version.of("v0.1").incomplete().eq("v0.1.2");   // true
 * </pre>
 * 
 * @title VersionComparator.java
 * @description 
 * @author 卢春梦
 * @version 1.0
 * @created 2015年7月6日下午3:25:51
 */
public class Version {

	private static final String delimiter = "\\.";

	// 版本号
	private String version;
	// 是否完整模式，默认使用完整模式
	private boolean complete = true;

	/**
	 * 私有实例化构造方法
	 */
	private Version() {}
	private Version(String version) {
		this.version = version;
	}

	/**
	 * 不完整模式
	 * @return {Version}
	 */
	public Version incomplete() {
		this.complete = false;
		return this;
	}

	/**
	 * 构造器
	 * @param version
	 */
	public static Version of(String version) {
		return new Version(version);
	}

	/**
	 * 比较版本号是否相同
	 * 
	 * example:
	 * <pre>
	 * Version.of("v0.3").eq("v0.4")
	 * </pre>
	 * 
	 * @param version 字符串版本号
	 * @return {boolean}
	 */
	public boolean eq(String version) {
		return compare(version) == 0;
	}

	public boolean gt(String version) {
		return compare(version) > 0;
	}

	public boolean gte(String version) {
		return compare(version) >= 0;
	}

	public boolean lt(String version) {
		return compare(version) < 0;
	}

	public boolean lte(String version) {
		return compare(version) <= 0;
	}

	/**
	 * 和另外一个版本号比较
	 * @param version
	 * @return int
	 */
	private int compare(String version) {
		return Version.compare(this.version, version, complete);
	}

	/**
	 * 比较2个版本号
	 * @param v1
	 * @param v2
	 * @param complete 是否完整的比较两个版本  
	 * 
	 * @return (v1 < v2) ? -1 : ((v1 == v2) ? 0 : 1)
	 */
	private static int compare(String v1, String v2, boolean complete) {
		if (v1.equals(v2)) {
			return 0;
		}
		String[] v1s = v1.split(delimiter);
		String[] v2s = v2.split(delimiter);
		int len = complete 
				? Math.max(v1s.length, v2s.length) 
				: Math.min(v1s.length, v2s.length);

		for (int i = 0; i < len; i++) {
			String c1 = null == v1s[i] ? "" : v1s[i];
			String c2 = null == v2s[i] ? "" : v2s[i];

			int result = c1.compareTo(c2);
			if (result != 0) return result;
		}

		return 0;
	}

}
