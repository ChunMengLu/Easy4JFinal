/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package net.dreamlu.easy.commons.owner;

import java.io.Serializable;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.InvocationHandler;

/**
 * This {@link InvocationHandler} receives method calls from the delegate instantiated by {@link ConfigFactory} and maps
 * it to a property value from a property file, or a {@link Config.DefaultValue} specified in method annotation.
 * <p>
 * The {@link Config.Key} annotation can be used to override default mapping between method names and property names.
 * </p>
 * <p>
 * Automatic conversion is handled between the property value and the return type expected by the method of the
 * delegate.
 * </p>
 *
 * @author Luigi R. Viggiano
 */
class PropertiesInvocationHandler implements InvocationHandler, Serializable {
    private static final long serialVersionUID = 5432212884255718342L;

    private final StrSubstitutor substitutor;
    final PropertiesManager propertiesManager;

    PropertiesInvocationHandler(PropertiesManager manager) {
        this.propertiesManager = manager;
        this.substitutor = new StrSubstitutor(manager.load());
    }

    public Object invoke(Object proxy, Method invokedMethod, Object[] args) throws Throwable {
        if (invokedMethod.getDeclaringClass() == Object.class) {  
            return invokedMethod.invoke(this, args);  
        }
        return resolveProperty(invokedMethod, args);
    }

    private Object resolveProperty(Method method, Object... args) {
        String key = expandKey(method);
        String value = propertiesManager.getProperty(key);
        if (value == null) {
            return null;
        }
        String text = format(method, expandVariables(method, value), args);
        Object result = Converters.convert(method, method.getReturnType(), text);
        if (result == Converters.SpecialValue.NULL) return null;
        return result;
    }

    private String expandKey(Method method) {
        String key = PropertiesMapper.key(method);
        return substitutor.replace(key);
    }

    private String format(Method method, String format, Object... args) {
        return String.format(format, args);
    }

    private String expandVariables(Method method, String value) {
        return substitutor.replace(value);
    }

}
