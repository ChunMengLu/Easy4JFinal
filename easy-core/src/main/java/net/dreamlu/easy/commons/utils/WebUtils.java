package net.dreamlu.easy.commons.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;

import net.dreamlu.easy.model.User;

/**
 * Web相关工具类
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年7月5日下午7:48:48
 */
public final class WebUtils {

	private WebUtils() {}

	/**
	 * 密码:md5(sha1(pwd))
	 * @param pwd 密码
	 * @return String
	 */
	public static String pwdEncode(String pwd) {
		return HashKit.md5(HashKit.sha1(pwd));
	}

	private static String USER_COOKIE_KEY    = "uid";
	private static String USER_COOKIE_SECRET = "&#%!&*";

	/**
	 * 设置用户账户cookie name
	 * @param userKey
	 */
	public static void setUserKey(String userKey) {
		if (StrKit.isBlank(userKey)) {
			return;
		}
		USER_COOKIE_KEY = userKey;
	}

	/**
	 * 设置用户账户cookie 密钥
	 * @param userSecret
	 */
	public static void setUserSecret(String userSecret) {
		if (StrKit.isBlank(userSecret)) {
			return;
		}
		USER_COOKIE_SECRET = userSecret;
	}

	/**
	 * 返回当前用户信息
	 * 
	 * @param c
	 * @return User
	 */
	public static User getCurrentUser(Controller c) {
		HttpServletRequest  request  = c.getRequest();
		HttpServletResponse response = c.getResponse();
		return getCurrentUser(request, response);
	}

	/**
	 * 返回当前用户
	 * @param request
	 * @param response
	 * @return User
	 */
	public static User getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
		String cookieKey = USER_COOKIE_KEY;
		// 获取cookie信息
		String userCookie = getCookie(request, cookieKey);
		// 1.cookie为空，直接清除
		if (StrKit.isBlank(userCookie)) {
			removeCookie(response, cookieKey);
			return null;
		}
		// 2.解密cookie
		String cookieInfo = null;
		try {
			cookieInfo = AESUtils.decrypt(USER_COOKIE_SECRET, userCookie);
		} catch (RuntimeException e) {
			// ignore
		}
		// 3.异常或解密问题，直接清除cookie信息
		if (StrKit.isBlank(cookieInfo)) {
			removeCookie(response, cookieKey);
			return null;
		}
		String[] userInfo = cookieInfo.split("~");
		// 4.规则不匹配
		if (userInfo.length < 3) {
			removeCookie(response, cookieKey);
			return null;
		}
		String userId   = userInfo[0];
		String oldTime  = userInfo[1];
		String maxAge   = userInfo[2];
		// 5.判定时间区间，超时的cookie清理掉
		if (!"-1".equals(maxAge)) {
			long now  = System.currentTimeMillis();
			long time = Long.parseLong(oldTime) + (Long.parseLong(maxAge) * 1000);
			if (time <= now) {
				removeCookie(response, cookieKey);
				return null;
			}
		}
		return User.dao.loadInSession(userId);
	}

	/**
	 * 用户登陆状态维持
	 * 
	 * cookie设计为: des(私钥).encode(userId~time~maxAge~ip)
	 * 
	 * @param c 控制器
	 * @param user  用户model
	 * @param remember   是否记住密码、此参数控制cookie的 maxAge，默认为-1（只在当前会话）<br>
	 *                   记住密码默认为一周
	 * @return void
	 */
	public static void loginUser(Controller c, User user, boolean... remember) {
		// 获取用户的id
		String uid     = user.getUserId() + "";
		// 当前毫秒数
		long   now      = System.currentTimeMillis();
		// 超时时间
		int    maxAge   = -1;
		if (remember.length > 0 && remember[0]) {
			maxAge      = 60 * 60 * 24 * 7;
		}
		// 用户id地址
		String ip		= getIP(c);
		// 构造cookie
		StringBuilder cookieBuilder = new StringBuilder()
			.append(uid).append("~")
			.append(now).append("~")
			.append(maxAge).append("~")
			.append(ip);

		// 加密cookie
		String userCookie = AESUtils.encrypt(USER_COOKIE_SECRET, cookieBuilder.toString());

		// 设置用户的cookie、 -1 维持成session的状态
		setCookie(c.getResponse(), USER_COOKIE_KEY, userCookie, maxAge);
	}

	/**
	 * 退出即删除用户信息
	 * @param c 控制器
	 */
	public static void logoutUser(Controller c) {
		HttpServletResponse response = c.getResponse();
		removeCookie(response, USER_COOKIE_KEY);
	}

	/**
	 * 读取cookie
	 * @param request
	 * @param key
	 * @return String
	 */
	public static String getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if(null != cookies){
			for (Cookie cookie : cookies) {
				if (key.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 清除 某个指定的cookie 
	 * @param response
	 * @param key
	 */
	public static void removeCookie(HttpServletResponse response, String key) {
		setCookie(response, key, null, null, 0);
	}

	/**
	 * 设置cookie
	 */
	public static void setCookie(HttpServletResponse response, String name, 
            String value, int maxAgeInSeconds) {
	    setCookie(response, name, value, null, maxAgeInSeconds);
	}
	
	/**
	 * 设置cookie
	 */
	public static void setCookie(HttpServletResponse response, String name, 
	        String value, String domain, int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		if (StrKit.notBlank(domain)) {
		    cookie.setDomain(domain);
		}
		// 指定为httpOnly保证安全性
		int version = JFinal.me().getServletContext().getMajorVersion();
		if (version >= 3) {
			cookie.setHttpOnly(true);
		}
		response.addCookie(cookie);
	}

	/**
	 * 获取浏览器信息
	 * @param c
	 * @return String
	 */
	public static String getUserAgent(Controller c) {
		return getUserAgent(c.getRequest());
	}

	/**
	 * 获取浏览器信息
	 * @param request
	 * @return String
	 */
	public static String getUserAgent(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}

	/**
	 * 获取ip
	 * @param c
	 * @return
	 */
	public static String getIP(Controller c) {
		HttpServletRequest request = c.getRequest();
		return getIP(request);
	}

	/**
	 * 获取ip
	 * @param request
	 * @return
	 */
	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Requested-For");
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StrKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return StrKit.isBlank(ip) ? null : ip.split(",")[0];
	}

}
