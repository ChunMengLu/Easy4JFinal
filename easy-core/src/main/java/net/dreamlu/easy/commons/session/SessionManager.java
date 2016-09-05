package net.dreamlu.easy.commons.session;

public interface SessionManager {
    /**
     * 获取EasySession
     * @param sessionId sessionId
     * @return {EasySession}
     */
    EasySession get(String sessionId);
    
    /**
     * 保存 EasySession
     * @param session EasySession
     */
    void save(EasySession session);
    
    /**
     * 更新 EasySession
     * @param session EasySession
     */
    void update(EasySession session);
}
