package net.dreamlu.easy.commons.session;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import com.jfinal.kit.StrKit;

import net.dreamlu.easy.commons.config.EasyConstants;
import net.dreamlu.easy.commons.utils.WebUtils;

public class SessionRepositoryRequestWrapper extends HttpServletRequestWrapper {
    private static SessionManager sessionManager = EasyConstants.getSessionManager();
    
    public SessionRepositoryRequestWrapper(HttpServletRequest request) {
        super(request);
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
        String sessionId = getSessionId();
        if (StrKit.isBlank(sessionId)) {
            sessionId = UUID.randomUUID().toString();
        }
        return sessionId;
    }

    @Override
    public HttpSession getSession() {
        String sessionId = getRequestedSessionId();
        return sessionManager.get(sessionId);
    }

    @Override
    public HttpSession getSession(boolean create) {
        String sessionId = getRequestedSessionId();
        HttpSession session = sessionManager.get(sessionId);
        if (null == session && create) {
            session = new EasySession(sessionId);
            sessionManager.put(sessionId, session);
        }
        return session;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        String sessionId = getSessionId();
        return StrKit.notBlank(sessionId);
    }

}
