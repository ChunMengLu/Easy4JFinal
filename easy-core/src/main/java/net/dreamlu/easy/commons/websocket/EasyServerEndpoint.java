package net.dreamlu.easy.commons.websocket;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.jfinal.log.Log;

import net.dreamlu.easy.commons.plugin.event.EventKit;
import net.dreamlu.easy.commons.utils.LinkedMultiMap;

/**
 * websocket简单封装，用于服务器和浏览器交互
 * @author L.cm
 */
@ServerEndpoint(
    value = "/ws/{msgId}",
    decoders = WsMessageDecoder.class,
    encoders = WsMessageEncoder.class
)
@SuppressWarnings("rawtypes")
public class EasyServerEndpoint {
    private static final Log logger = Log.getLog(EasyServerEndpoint.class);
    
    // 因为用户浏览器打开多tab页面的时候会产生多个session
    private static LinkedMultiMap<String, Session> sessionMap;
    // 10分钟的超时时间
    public static long timeout = 10 * 60 * 1000;
    
    public EasyServerEndpoint() {
        ConcurrentMap<String, List<Session>> map = new ConcurrentHashMap<String, List<Session>>();
        sessionMap = new LinkedMultiMap<String, Session>(map);
        WebSocketKit.init(sessionMap);
    }
    
    @OnOpen
    public void open(Session session, @PathParam("msgId") String msgId) {
        session.setMaxIdleTimeout(timeout);
        String userId = WebSocketKit.decryptMsgId(msgId);
        sessionMap.put(userId, session);
        WebSocketKit.send(session, new WsMessage());
    }
    
    @OnMessage
    public void onMessage(Session session, @PathParam("msgId") String msgId, WsMessage msg) {
        if (msg == null) {
            WebSocketKit.send(session, new WsMessage("msg is null!"));
        }
        String userId = WebSocketKit.decryptMsgId(msgId);
        msg.setUserId(userId);
        EventKit.post(new WebSocketEvent(msg));
    }
    
    @OnError
    public void error(Session session, @PathParam("msgId") String msgId, Throwable t) {
        WebSocketKit.send(session, new WsMessage(t.getMessage()));
        logger.error(t.getMessage(), t);
    }
    
    @OnClose
    public void close(Session session, @PathParam("msgId") String msgId, CloseReason reason) {
        String userId = WebSocketKit.decryptMsgId(msgId);
        removeSession(session, userId);
    }
    
    public boolean removeSession(Session session, String userId) {
        List<Session> sessionList = sessionMap.get(userId);
        return sessionList.contains(session) && sessionList.remove(session);
    }

}
