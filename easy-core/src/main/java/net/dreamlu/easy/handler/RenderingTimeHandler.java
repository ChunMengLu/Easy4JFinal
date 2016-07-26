package net.dreamlu.easy.handler;

import com.jfinal.handler.Handler;
import com.jfinal.log.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <a href = http://www.oschina.net/question/173052_62229 />
 * 
 * @author kid
 * 
 */
public class RenderingTimeHandler extends Handler {

    protected final Log log = Log.getLog(getClass());

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        long start = System.currentTimeMillis();
        String userAgent = request.getHeader("User-Agent");
        next.handle(target, request, response, isHandled);
        long end = System.currentTimeMillis();
        log.info("User-Agent:["+ userAgent + "]\tURL:["+ target + "]\tTRENDING TIME:\t[" + (end - start) + "]ms");
    }

}
