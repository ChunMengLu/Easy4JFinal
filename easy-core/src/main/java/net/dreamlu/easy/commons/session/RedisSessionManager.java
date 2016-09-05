package net.dreamlu.easy.commons.session;

import com.jfinal.plugin.redis.Redis;

public class RedisSessionManager implements SessionManager {

    @Override
    public EasySession get(String sessionId) {
        return Redis.use().get(sessionId);
    }

    @Override
    public void save(EasySession session) {
        Redis.use().set(session.getId(), session);
    }

    @Override
    public void update(EasySession session) {
        Redis.use().set(session.getId(), session);
    }

}
