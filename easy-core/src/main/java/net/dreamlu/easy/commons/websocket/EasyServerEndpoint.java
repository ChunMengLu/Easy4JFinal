package net.dreamlu.easy.commons.websocket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.jfinal.log.Log;

import net.dreamlu.easy.commons.utils.LinkedMultiMap;

/**
 * websocket简单封装，用于服务器和浏览器交互
 * @author L.cm
 */
@ServerEndpoint(
    value = "/easy/{userId}/ws.ws",
    decoders = WsMessageDecoder.class,
    encoders = WsMessageEncoder.class
)
public class EasyServerEndpoint {
    private static final Log logger = Log.getLog(EasyServerEndpoint.class);
    
    // 因为用户浏览器打开多tab页面的时候会产生多个session
    static LinkedMultiMap<String, Session> sessionMap;
    // 10分钟的超时时间
    public static long timeout = 10 * 60 * 1000;
    
    public EasyServerEndpoint() {
        ConcurrentMap<String, List<Session>> map = new ConcurrentHashMap<String, List<Session>>();
        sessionMap = new LinkedMultiMap<String, Session>(map);
    }

    @OnOpen
    public void open(Session session, @PathParam("userId") String userId) {
        try {
          session.setMaxIdleTimeout(timeout);
          session.getBasicRemote().sendObject(new WsMessage("Welcome"));
        } catch (IOException e) {
        logger.error(e.toString());
        } catch (EncodeException e) {
            logger.error(e.toString());
        }
        sessionMap.put(userId, session);
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("userId") String userId, WsMessage msg) {
        if (msg != null) {

        }
    }

    @OnError
    public void error(Session session, @PathParam("userId") String userId, Throwable t) {
        removeSession(session, userId);
    }

    @OnClose
    public void close(Session session, @PathParam("userId") String userId, CloseReason reason) {
        System.out.println(userId);
        removeSession(session, userId);
    }

    public boolean removeSession(Session session, String userId) {
        List<Session> sessionList = sessionMap.get(userId);
        return sessionList.contains(session) && sessionList.remove(session);
    }

}
