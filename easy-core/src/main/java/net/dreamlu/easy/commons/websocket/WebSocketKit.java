package net.dreamlu.easy.commons.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import com.jfinal.kit.HashKit;
import com.jfinal.log.Log;

import net.dreamlu.easy.commons.utils.AESUtils;
import net.dreamlu.easy.commons.utils.LinkedMultiMap;

@SuppressWarnings("rawtypes")
public class WebSocketKit {
    private static final Log logger = Log.getLog(WebSocketKit.class);
    private static LinkedMultiMap<String, Session> sessionMap;
    // salt
    private static String salt = HashKit.generateSalt(32);
    
    static void init(LinkedMultiMap<String, Session> sessionMap) {
        WebSocketKit.sessionMap = sessionMap;
    }
    
    /**
     * 解密消息Id
     * @param msgId 消息id
     * @return 用户id
     */
    static String decryptMsgId(String msgId) {
        return AESUtils.decrypt(salt, msgId);
    }
    
    /**
     * 获取消息Id
     * @param userId 用户id
     * @return 消息id
     */
    public static String getMsgId(String userId) {
        return AESUtils.encrypt(salt, userId);
    }
    
    /**
     * 后台向前台推送消息，所有连接均消费
     * @param msg 消息
     */
    public static void pushAll(WsMessage msg) {
        Set<Entry<String, List<Session>>> set = sessionMap.entrySet();
        for (Entry<String, List<Session>> entry : set) {
            List<Session> sessionList = entry.getValue();
            for (Session session : sessionList) {
                send(session, msg);
            }
        }
    }
    
    /**
     * 后台向前台指定用户推送消息
     * @param userId 用户id
     * @param msg 消息
     */
    public static void push(String userId, WsMessage msg) {
        List<Session> sessionList = sessionMap.get(userId);
        if (null == sessionList) {
            throw new RuntimeException("userId:" + userId + " is not onling!");
        }
        for (Session session : sessionList) {
            WebSocketKit.send(session, msg);
        }
    }
    
    protected static void send(Session session, WsMessage msg) {
        try {
            session.getBasicRemote().sendObject(msg);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (EncodeException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
