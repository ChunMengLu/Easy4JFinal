package net.dreamlu.easy.commons.websocket;

import java.io.Serializable;

/**
 * websocket消息
 * @author L.cm
 */
public class WsMessage<T> implements Serializable {
    private static final long serialVersionUID = 4812048756511348452L;

    /**
     * 是否成功
     */
    private boolean success = true;
    /**
     * 错误消息
     */
    private String msg;
    /**
     * 携带的数据
     */
    private T data;
    /**
     * 携带的用户Id
     */
    private transient String userId;

    public WsMessage() {}
    
    public WsMessage(String msg) {
        this.success = false;
        this.msg = msg;
    }
    
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

}
