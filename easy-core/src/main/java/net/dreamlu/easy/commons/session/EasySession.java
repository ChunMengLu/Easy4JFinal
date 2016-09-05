package net.dreamlu.easy.commons.session;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.jfinal.core.JFinal;

/**
 * EasySession
 * 
 * 参考：org.apache.catalina.session.StandardSession
 * 
 * @author L.cm
 */
@SuppressWarnings({"rawtypes", "unchecked"})
class EasySession implements HttpSession, Serializable {
    private static final long serialVersionUID = -719739616470614546L;
    
    protected String id = null;
    protected final Map<String, Object> attributes = new ConcurrentHashMap();
    protected final long creationTime = System.currentTimeMillis();
    protected volatile int maxInactiveInterval = -1;
    protected volatile boolean isNew = false;
    protected transient SessionManager manager;
    
    public EasySession(String id) {
        this.id = id;
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public Enumeration getAttributeNames() {
        Set<String> names = new HashSet();
        names.addAll(this.attributes.keySet());
        return Collections.enumeration(names);
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public String getId() {
        return this.id;
    }

    public long getLastAccessedTime() {
        return this.creationTime;
    }

    public int getMaxInactiveInterval() {
        return getMaxInactiveInterval();
    }

    public ServletContext getServletContext() {
        return JFinal.me().getServletContext();
    }

    @Deprecated
    public javax.servlet.http.HttpSessionContext getSessionContext() {
        return null;
    }

    @Deprecated
    public Object getValue(String key) {
        return getAttribute(key);
    }

    @Deprecated
    public String[] getValueNames() {
        return (String[]) this.attributes.keySet().toArray(new String[0]);
    }

    public void invalidate() {}

    public boolean isNew() {
        return isNew;
    }

    @Deprecated
    public void putValue(String key, Object value) {
        setAttribute(key, value);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
        manager.update(this);
    }

    @Deprecated
    public void removeValue(String key) {
        removeAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("setAttribute: name parameter cannot be null");
        }
        if (value == null) {
            removeAttribute(key);
            return;
        }
        attributes.put(key, value);
        manager.update(this);
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    public void setManager(SessionManager manager) {
        this.manager = manager;
    }
}