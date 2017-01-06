package net.dreamlu.easy.commons.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

/**
 * 使用owner解析application.properties文件
 * @author L.cm
 */
@Sources("classpath:application.properties")
public interface ApplicationConfig extends Config {
	
	@Key("app.devMode")
	@DefaultValue("false")
	boolean devMode();
	
	@Key("app.encoding")
	@DefaultValue("UTF-8")
	String encoding();
	
	@Key("app.max-post-size")
	@DefaultValue("10485760") // 1024 * 1024 * 10    10M
	int maxPostSize();

	@Key("base.download-path")
	@DefaultValue("download")
	String downloadPath();
	
	@Key("base.upload-path")
	@DefaultValue("uploads")
	String uploadPath();
	
	@Separator(";")
	@DefaultValue("")
	@Key("ioc.scan.pkg")
	String[] iocScanPkg();
	
	@Key("json.date-pattern")
	@DefaultValue("yyyy-MM-dd HH:mm:ss")
	String jsonDatePattern();
	
	@Key("user.key")
	@DefaultValue("easyid")
	String userKey();
	
	@Key("user.secret")
	@DefaultValue("Easy4JFinal")
	String userSecret();
	
	@Key("view.type")
	@DefaultValue("beetl")
	String viewType();
	
	@Key("view.prefix")
	@DefaultValue("/WEB-INF/view/")
	String viewPrefix();

	@Key("dev.urlPrefix")
	@DefaultValue("/_dev")
	String devUrlPrefix();
	
	@Key("dev.devDir")
	@DefaultValue("_dev")
	String devDevDir();
	
	@Separator(";")
	@Key("app.xmlsql.pkg")
	String[] xmlSqlPkg();
	
	@Separator(";")
	@Key("app.event.pkg")
	String[] eventPkg();

	@Key("sqls.file")
	String sqlsFile();
	//session=================================================================//
	@Key("session.enable")
	@DefaultValue("false")
	boolean sessionEnable(); // 是否启用Session
	
	@Key("session.manager")
	@DefaultValue("redis:session")
	String sessionManager();
	
	@Key("session.timeout")
	@DefaultValue("30")
	int sessionTimeout();
	
	@Key("session.cookie.name")
	@DefaultValue("easy_session")
	String sessionCookieName();
	
	@Key("session.cookie.domain")
	String sessionCookieDomain();
	//db======================================================================//
	@Key("db.default.url")
	String dbDefaultUrl();

	@Key("db.default.user")
	String dbDefaultUser();
	
	@Key("db.default.password")
	String dbDefaultPwd();
	
	@Key("db.default.driver")
	String dbDefaultDriver();
	//weixin==================================================================//
	@Key("wx.enable")
	@DefaultValue("false")
	boolean wxEnable(); // 是否启用weixin
	
	@Key("wx.devMode")
	@DefaultValue("false")
	boolean wxDevMode();
	
	@Key("wx.default.token")
	String wxDefaultToken();
	
	@Key("wx.default.appId")
	String wxDefaultAppId();
	
	@Key("wx.default.appSecret")
	String wxDefaultAppSecret();
	
	@Key("wx.default.encryptMessage")
	@DefaultValue("false")
	boolean wxDefaultEncryptMessage();
	
	@Key("wx.default.encodingAesKey")
	String wxDefaultEncodingAesKey();

}
