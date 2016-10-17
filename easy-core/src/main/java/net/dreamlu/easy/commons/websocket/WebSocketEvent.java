package net.dreamlu.easy.commons.websocket;

import net.dreamlu.easy.commons.plugin.event.core.ApplicationEvent;

/**
 * WebSocket 事件
 * @author L.cm
 */
public class WebSocketEvent extends ApplicationEvent {
    private static final long serialVersionUID = -6148949762934672066L;

    @SuppressWarnings("rawtypes")
    public WebSocketEvent(WsMessage source) {
        super(source);
    }

}
