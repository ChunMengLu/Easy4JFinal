package net.dreamlu.easy.commons.config;

import net.dreamlu.easy.commons.core.EasyConst;
import net.dreamlu.easy.commons.session.SessionManager;

/**
 * 
 * @author L.cm
 */
public final class EasyConstants {
    private String userKey              = EasyConst.USER_KEY;
    private String userSecret           = EasyConst.USER_SECRET;
    private String devUrlPrefix         = EasyConst.DEV_URL_PREFIX;
    private String devDir               = EasyConst.DEV_DIR;

    private int sessionTimeout          = EasyConst.SESSION_TIMEOUT;
    private boolean sessionEnable       = EasyConst.SESSION_ENABLE;
    private String sessionManagerName   = EasyConst.SESSION_MANAGER;
    private String sessionRedisName     = EasyConst.SESSION_REDIS_NAME;
    private String sessionCookieName    = EasyConst.SESSION_COOKIE_NAME;
    private String sessionCookieDomain  = null;
    
    private SessionManager sessionManager = null;
    
    public static final EasyConstants me = new EasyConstants();
    
    private EasyConstants() {}
    
    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public String getUserSecret() {
        return userSecret;
    }
    public void setUserSecret(String userSecret) {
        this.userSecret = userSecret;
    }
    public String getDevUrlPrefix() {
        return devUrlPrefix;
    }
    public void setDevUrlPrefix(String devUrlPrefix) {
        this.devUrlPrefix = devUrlPrefix;
    }
    public String getDevDir() {
        return devDir;
    }
    public void setDevDir(String devDir) {
        this.devDir = devDir;
    }
    public boolean isSessionEnable() {
        return sessionEnable;
    }
    public void setSessionEnable(boolean sessionEnable) {
        this.sessionEnable = sessionEnable;
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
    public SessionManager getSessionManager() {
        return sessionManager;
    }
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    
}
