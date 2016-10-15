package net.dreamlu.easy.commons.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.alibaba.fastjson.JSON;

/**
 * websocket消息编码
 * @author L.cm
 */
public class WsMessageEncoder implements Encoder.Text<WsMessage> {

    @Override
    public void init(EndpointConfig config) {
        
    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(WsMessage object) throws EncodeException {
        return JSON.toJSONString(object);
    }

}
