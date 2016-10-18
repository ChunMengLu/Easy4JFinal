package net.dreamlu.easy.commons.config;

import com.jfinal.config.Constants;
import com.jfinal.kit.Prop;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Sqls;

import net.dreamlu.easy.commons.core.EasyConst;
import net.dreamlu.easy.commons.session.RedisSessionManager;

/**
 * 参数转换处理
 * @author L.cm
 */
class ConfigParser {

    /**
     * 该部分代码太多生硬
     */
    public static void parser(Prop prop, Constants me, EasyConstants easyConst) {
        String encoding = prop.get("app.encoding");
        if (StrKit.notBlank(encoding)) {
            me.setEncoding(encoding);
        }
        Integer maxPostSize = prop.getInt("app.max-post-size");
        if (null != maxPostSize && maxPostSize > 0) {
            me.setMaxPostSize(maxPostSize);
        }
        // 暂时不解析这2个字段
//        String viewType     = prop.get("view.type");
//        String viewPrefix   = prop.get("view.prefix");
        
        String baseDownloadPath = prop.get("base.download-path");
        String baseUploadPath   = prop.get("base.upload-path");
        String jsonDatePattern  = prop.get("json.date-pattern");
        if (StrKit.notBlank(baseDownloadPath)) {
            me.setBaseDownloadPath(baseDownloadPath);
        }
        if (StrKit.notBlank(baseUploadPath)) {
            me.setBaseUploadPath(baseUploadPath);
        }
        if (StrKit.notBlank(jsonDatePattern)) {
            me.setJsonDatePattern(jsonDatePattern);
        }
        
        String userKey    = prop.get("user.key");
        String userSecret = prop.get("user.secret");
        if (StrKit.notBlank(userKey)) {
            easyConst.setUserKey(userKey);
        }
        if (StrKit.notBlank(userSecret)) {
            easyConst.setUserSecret(userSecret);
        }
        
        String devUrlPrefix = prop.get("dev.urlPrefix");
        String devDir = prop.get("dev.devDir");
        if (StrKit.notBlank(devUrlPrefix)) {
            easyConst.setDevUrlPrefix(devUrlPrefix);
        }
        if (StrKit.notBlank(devDir)) {
            easyConst.setDevDir(devDir);
        }
        
        boolean sessionEnable = prop.getBoolean("session.enable", EasyConst.SESSION_ENABLE);
        if (sessionEnable) {
            easyConst.setSessionEnable(sessionEnable);
        }
        String sessionManager = prop.get("session.manager");
        if (StrKit.notBlank(sessionManager)) {
            if (sessionManager.equalsIgnoreCase("redis")) {
                easyConst.setSessionManager(new RedisSessionManager());
            }
        }
        Integer sessionTimeout = prop.getInt("session.timeout");
        if (null != sessionTimeout) {
            easyConst.setSessionTimeout(sessionTimeout);
        }
        String sessionCookieName = prop.get("session.cookie.name");
        if (StrKit.notBlank(sessionCookieName)) {
            easyConst.setSessionCookieName(sessionCookieName);
        }
        String sessionCookieDomain = prop.get("session.cookie.domain");
        if (StrKit.notBlank(sessionCookieDomain)) {
            easyConst.setSessionCookieDomain(sessionCookieDomain);
        }
        
        String xmlSqlPkg = prop.get("scan.xmlsql.pkg");
        if (StrKit.notBlank(xmlSqlPkg)) {
            easyConst.setXmlSqlPkg(xmlSqlPkg);
        }
        String eventPkg = prop.get("scan.event.pkg");
        if (StrKit.notBlank(xmlSqlPkg)) {
            easyConst.setEventPkg(eventPkg);
        }
        String sqlsFile = prop.get("sqls.file");
        if (StrKit.notBlank(sqlsFile)) {
            Sqls.load(sqlsFile);
        }
    }
    
}
