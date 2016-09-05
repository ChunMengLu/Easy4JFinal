package net.dreamlu.easy.commons.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

/**
 * 自己实现session
 * @author L.cm
 */
public class SessionHandler extends Handler {

    @Override
    public void handle(String target, HttpServletRequest request, 
            HttpServletResponse response, boolean[] isHandled) {
        
        request = new SessionRepositoryRequestWrapper(request, response);
        next.handle(target, request, response, isHandled);
    }

}
