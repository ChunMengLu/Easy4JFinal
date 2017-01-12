/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package net.dreamlu.easy.commons.owner;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Luigi R. Viggiano
 */
class ConfigURIFactory {

    private static final String CLASSPATH_PROTOCOL = "classpath:";
    private static final String FILE_PROTOCOL = "file:";
    private final transient ClassLoader classLoader;
    private final VariablesExpander expander;

    ConfigURIFactory(ClassLoader classLoader, VariablesExpander expander) {
        this.classLoader = classLoader;
        this.expander = expander;
    }

    URI newURI(String spec) throws URISyntaxException {
        String expanded = expand(spec);
        String fixed = Util.fixBackslashesToSlashes(expanded);
        if (fixed.startsWith(CLASSPATH_PROTOCOL)) {
            String path = fixed.substring(CLASSPATH_PROTOCOL.length());
            URL url = classLoader.getResource(path);
            if (url == null)
                return null;
            return url.toURI();
        } else if (fixed.startsWith(FILE_PROTOCOL)) {
            String path = Util.fixSpacesToPercentTwenty(fixed);
            return new URI(path);
        } else {
            return new URI(fixed);
        }
    }

    private String expand(String path) {
        return expander.expand(path);
    }

    String toClasspathURLSpec(String name) {
        return CLASSPATH_PROTOCOL + name.replace('.', '/');
    }

}
