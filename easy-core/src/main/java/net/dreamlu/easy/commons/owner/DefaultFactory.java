/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package net.dreamlu.easy.commons.owner;

import java.util.Map;
import java.util.Properties;

import net.dreamlu.easy.commons.owner.loaders.Loader;
import net.sf.cglib.proxy.Proxy;

/**
 * Default implementation for {@link Factory}.
 *
 * @author Luigi R. Viggiano
 */
class DefaultFactory implements Factory {
    private Properties props;
    final LoadersManager loadersManager;

    DefaultFactory(Properties props) {
        this.props = props;
        this.loadersManager = new LoadersManager();
    }

    @SuppressWarnings("unchecked")
    public <T extends Config> T create(Class<? extends T> clazz, Map<?, ?>... imports) {
        Class<?>[] interfaces = interfaces(clazz);
        VariablesExpander expander = new VariablesExpander(props);
        PropertiesManager manager = new PropertiesManager(clazz, new Properties(), expander, loadersManager,
                imports);
        PropertiesInvocationHandler handler = new PropertiesInvocationHandler(manager);
        T proxy = (T) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, handler);
        return proxy;
    }

    public String setProperty(String key, String value) {
        checkKey(key);
        return (String) props.setProperty(key, value);
    }

    private void checkKey(String key) {
        if (key == null)
            throw new IllegalArgumentException("key can't be null");
        if (key.isEmpty())
            throw new IllegalArgumentException("key can't be empty");
    }

    public Properties getProperties() {
        return props;
    }

    public void setProperties(Properties properties) {
        if (properties == null)
            props = new Properties();
        else
            props = properties;
    }

    public void registerLoader(Loader loader) {
        loadersManager.registerLoader(loader);
    }

    public String getProperty(String key) {
        checkKey(key);
        return props.getProperty(key);
    }

    public String clearProperty(String key) {
        checkKey(key);
        return (String) props.remove(key);
    }

    private <T extends Config> Class<?>[] interfaces(Class<? extends T> clazz) {
        return new Class<?>[]{clazz};
    }

}
