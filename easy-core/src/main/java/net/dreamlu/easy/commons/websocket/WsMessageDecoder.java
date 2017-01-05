package net.dreamlu.easy.commons.websocket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.alibaba.fastjson.JSON;

import net.dreamlu.easy.commons.utils.StrUtils;

/**
 * websocket 消息解码
 * @author L.cm
 */
@SuppressWarnings("rawtypes")
public class WsMessageDecoder implements Decoder.Text<WsMessage> {

    @Override
    public void init(EndpointConfig config) {
        
    }

    @Override
    public void destroy() {
        
    }

    @Override
    public WsMessage decode(String s) throws DecodeException {
        return JSON.parseObject(s, WsMessage.class);
    }

    @Override
    public boolean willDecode(String str) {
        String json = StrUtils.trimToEmpty(str);
        return json.startsWith("{") && json.equals("}");
    }

}
