package net.dreamlu.easy.commons.core;

/**
 * 用户配置的常量初始值
 * @author L.cm
 */
public interface EasyConst {
    String EASY4JFINAL_VERSION      = "1.0";
    
    // 用户登陆cookie维持
    String USER_KEY                 = "EasyId";
    String USER_SECRET              = "Easy4JFinal";
    // 开发期间的配置
    String DEV_URL_PREFIX           = "/_dev";
    String DEV_DIR                  = "_dev";
    
    // session
    int SESSION_TIMEOUT             = 30;
    boolean SESSION_ENABLE          = false;
    String SESSION_MANAGER          = "redis";
    String SESSION_REDIS_NAME       = "session";
    String SESSION_COOKIE_NAME      = "EASY_SESSION";

}
