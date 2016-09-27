package net.dreamlu.easy.commons.session;

import javax.servlet.SessionCookieConfig;

import com.jfinal.core.JFinal;

/**
 * Esay Session 配置
 * @author Dreamlu
 *
 */
public class EasySessionConfig {
    private static SessionManager sessionManager;
    private static SessionCookieConfig sessionCookieConfig;
    
    static void init() {
        sessionCookieConfig = JFinal.me().getServletContext().getSessionCookieConfig();
    }
}
