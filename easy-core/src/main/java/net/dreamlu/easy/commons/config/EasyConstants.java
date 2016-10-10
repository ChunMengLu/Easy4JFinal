package net.dreamlu.easy.commons.config;

import net.dreamlu.easy.commons.core.EasyConst;
import net.dreamlu.easy.commons.session.EhcacheSessionManager;
import net.dreamlu.easy.commons.session.SessionManager;

/**
 * 
 * @author L.cm
 */
public final class EasyConstants {
    private String appUserKey          = EasyConst.APP_USER_KEY;
    private String appUserSecret       = EasyConst.APP_USER_SECRET;
    private String appDevUrlPrefix     = EasyConst.APP_DEV_URL_PREFIX;
    private String appDevDir           = EasyConst.APP_DEV_DIR;

    private int sessionTimeout         = EasyConst.SESSION_TIMEOUT;
    private String sessionCookieName   = EasyConst.SESSION_COOKIE_NAME;
    private String sessionCookieDomain = null;
    
    private static SessionManager sessionManager = new EhcacheSessionManager();
    
    public static final EasyConstants me = new EasyConstants();
    
    private EasyConstants() {}
    
    public String getAppUserKey() {
        return appUserKey;
    }
    public void setAppUserKey(String appUserKey) {
        this.appUserKey = appUserKey;
    }
    public String getAppUserSecret() {
        return appUserSecret;
    }
    public void setAppUserSecret(String appUserSecret) {
        this.appUserSecret = appUserSecret;
    }
    public String getAppDevUrlPrefix() {
        return appDevUrlPrefix;
    }
    public void setAppDevUrlPrefix(String appDevUrlPrefix) {
        this.appDevUrlPrefix = appDevUrlPrefix;
    }
    public String getAppDevDir() {
        return appDevDir;
    }
    public void setAppDevDir(String appDevDir) {
        this.appDevDir = appDevDir;
    }
    public int getSessionTimeout() {
        return sessionTimeout;
    }
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
    public String getSessionCookieName() {
        return sessionCookieName;
    }
    public void setSessionCookieName(String sessionCookieName) {
        this.sessionCookieName = sessionCookieName;
    }
    public String getSessionCookieDomain() {
        return sessionCookieDomain;
    }
    public void setSessionCookieDomain(String sessionCookieDomain) {
        this.sessionCookieDomain = sessionCookieDomain;
    }
    public static SessionManager getSessionManager() {
        return sessionManager;
    }
    
}
