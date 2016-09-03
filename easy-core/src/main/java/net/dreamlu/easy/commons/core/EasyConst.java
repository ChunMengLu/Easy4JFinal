package net.dreamlu.easy.commons.core;

/**
 * 用户配置的常量初始值
 * @author L.cm
 */
public interface EasyConst {
    String EASY4JFINAL_VERSION   = "2.3";
    
    // 用户登陆cookie维持
    String APP_USER_KEY          = "EasyId";
    String APP_USER_SECRET       = "Easy4JFinal";
    // 开发期间的配置
    String APP_DEV_URL_PREFIX    = "/_dev";
    String APP_DEV_DIR           = "_dev";
    
    // session
    int SESSION_TIMEOUT          = 30;
    String SESSION_COOKIE_NAME   = "EASY_SESSION";
    String SESSION_EHCACHE_NAME  = "session";
}
