package net.dreamlu.easy.commons.session;

import javax.servlet.http.HttpSession;

public interface SessionManager {
    EasySession get(String sessionId);
    
    void put(String sessionId, HttpSession session);
}
