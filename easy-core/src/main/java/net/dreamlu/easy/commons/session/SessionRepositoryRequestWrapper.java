package net.dreamlu.easy.commons.session;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jfinal.kit.StrKit;

import net.dreamlu.easy.commons.config.EasyConstants;
import net.dreamlu.easy.commons.utils.WebUtils;

public class SessionRepositoryRequestWrapper extends HttpServletRequestWrapper {
    private static SessionManager sessionManager = EasyConstants.getSessionManager();
    
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
        String appUserKey = EasyConstants.me.getAppUserKey();
        return WebUtils.getCookie(request, appUserKey);
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
            String appUserKey = EasyConstants.me.getAppUserKey();
            int maxAgeInSeconds = EasyConstants.me.getSessionTimeout() * 60;
            sessionId = UUID.randomUUID().toString();
            WebUtils.setCookie(response, appUserKey, sessionId, maxAgeInSeconds);
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
