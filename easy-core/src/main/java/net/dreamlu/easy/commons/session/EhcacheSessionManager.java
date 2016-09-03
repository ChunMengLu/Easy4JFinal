package net.dreamlu.easy.commons.session;

import javax.servlet.http.HttpSession;

import com.jfinal.plugin.ehcache.CacheKit;

import net.dreamlu.easy.commons.core.EasyConst;

/**
 * 默认cacheName 为session
 * @author L.cm
 */
public class EhcacheSessionManager implements SessionManager {
    @Override
    public EasySession get(String sessionId) {
        return CacheKit.get(EasyConst.SESSION_EHCACHE_NAME, sessionId);
    }

    @Override
    public void put(String sessionId, HttpSession session) {
        CacheKit.put(EasyConst.SESSION_EHCACHE_NAME, sessionId, session);
    }

}
