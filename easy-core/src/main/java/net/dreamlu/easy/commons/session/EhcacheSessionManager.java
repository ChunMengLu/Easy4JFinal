package net.dreamlu.easy.commons.session;

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
    public void save(EasySession session) {
        CacheKit.put(EasyConst.SESSION_EHCACHE_NAME, session.getId(), session);
    }

    @Override
    public void update(EasySession session) {
        CacheKit.put(EasyConst.SESSION_EHCACHE_NAME, session.getId(), session);
    }

}
