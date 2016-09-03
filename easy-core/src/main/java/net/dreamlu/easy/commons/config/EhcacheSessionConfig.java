package net.dreamlu.easy.commons.config;

import com.jfinal.plugin.ehcache.CacheKit;

import net.dreamlu.easy.commons.core.EasyConst;
import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;

class EhcacheSessionConfig {

    static void init() {
        // 默认超时时间为秒
        int maxTimeOut = EasyConstants.me.getSessionTimeout() * 60;
        
        CacheConfiguration config = new CacheConfiguration()
                .name(EasyConst.SESSION_EHCACHE_NAME)
                .maxEntriesLocalHeap(Integer.MAX_VALUE)
                .eternal(false)
                .overflowToDisk(false)
                .timeToIdleSeconds(maxTimeOut)
                .timeToLiveSeconds(maxTimeOut);
        
        Cache cache = new Cache(config);
        CacheKit.getCacheManager().addCache(cache);
    }
}
