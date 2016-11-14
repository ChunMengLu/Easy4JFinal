package net.dreamlu.easy.commons.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jfinal.kit.StrKit;

import net.dreamlu.easy.commons.config.ApplicationConfig;
import net.dreamlu.easy.commons.utils.StrUtils;
import net.dreamlu.easy.commons.utils.WebUtils;

public class SessionRepositoryRequestWrapper extends HttpServletRequestWrapper {
    private static SessionManager sessionManager;
    private static String sessionCookieDomain;
    private static String sessionCookieName;
    private static int maxAgeInSeconds;
    
    public static void initCfg(ApplicationConfig cfg) {
        sessionCookieDomain = cfg.sessionCookieDomain();
        sessionCookieName = cfg.sessionCookieName();
        maxAgeInSeconds = cfg.sessionTimeout() * 60;
        sessionManager = new RedisSessionManager(cfg.sessionManager().split(":")[1]);
    }
    
    private final HttpServletResponse response;
    
    public SessionRepositoryRequestWrapper(HttpServletRequest request, HttpServletResponse response) {
        super(request);
        this.response = response;
    }
    
    private HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) super.getRequest();
    }
    
    private String getSessionId() {
        HttpServletRequest request = getHttpRequest();
        return WebUtils.getCookie(request, sessionCookieName);
    }
    
    /**
     * 此处用cookie给重写掉
     */
    @Override
    public String getRequestedSessionId() {
        return getSessionId();
    }

    @Override
    public HttpSession getSession() {
        String sessionId = getRequestedSessionId();
        // 默认getSession(true)
        if (null == sessionId) {
            sessionId = StrUtils.getUUID();
            WebUtils.setCookie(response, sessionCookieName, sessionId, sessionCookieDomain, maxAgeInSeconds);
        }
        EasySession session = sessionManager.get(sessionId);
        if (null == session) {
            session = new EasySession(sessionId);
            sessionManager.save(session);
        }
        // 由于sessionManager不参与序列化，加上序列化的问题，故手动设置
        session.setManager(sessionManager);
        return session;
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (create) {
            return this.getSession();
        }
        String sessionId = getRequestedSessionId();
        if (null == sessionId) {
            return null;
        }
        EasySession session = sessionManager.get(sessionId);
        if (null != session) {
            // 由于sessionManager不参与序列化，加上序列化的问题，故手动设置
            session.setManager(sessionManager);
        }
        return session;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        String sessionId = getRequestedSessionId();
        return StrKit.notBlank(sessionId);
    }

}
