/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package net.dreamlu.easy.commons.owner;

import static java.lang.String.format;
import static java.net.URLDecoder.decode;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This class contains utility methods used all over the library.
 *
 * @author Luigi R. Viggiano
 */
abstract class Util {

    interface TimeProvider {
        long getTime();
    }

    interface SystemProvider {
        String getProperty(String key);

        Map<String, String> getenv();

        Properties getProperties();
    }

    static TimeProvider timeProvider = new TimeProvider() {
        public long getTime() {
            return System.currentTimeMillis();
        }
    };

    static SystemProvider system = new SystemProvider() {
        public String getProperty(String key) {
            return System.getProperty(key);
        }

        public Map<String, String> getenv() {
            return System.getenv();
        }

        public Properties getProperties() {
            return System.getProperties();
        }
    };

    /** Don't let anyone instantiate this class */
    private Util() {}

    static <T> List<T> reverse(List<T> src) {
        List<T> copy = new ArrayList<T>(src);
        Collections.reverse(copy);
        return copy;
    }

    static <T> T[] reverse(T[] array) {
        T[] copy = array.clone();
        Collections.reverse(asList(copy));
        return copy;
    }

    static String expandUserHome(String text) {
        if (text.equals("~"))
            return system.getProperty("user.home");
        if (text.indexOf("~/") == 0 || text.indexOf("file:~/") == 0 || text.indexOf("jar:file:~/") == 0)
            return text.replaceFirst("~/", fixBackslashForRegex(system.getProperty("user.home")) + "/");
        if (text.indexOf("~\\") == 0 || text.indexOf("file:~\\") == 0 || text.indexOf("jar:file:~\\") == 0)
            return text.replaceFirst("~\\\\", fixBackslashForRegex(system.getProperty("user.home")) + "\\\\");
        return text;
    }

    static String fixBackslashForRegex(String text) {
        return text.replace("\\", "\\\\");
    }

    public static String fixBackslashesToSlashes(String path) {
        return path.replace('\\', '/');
    }

    public static String fixSpacesToPercentTwenty(String path) {
        return path.replace(" ", "%20");
    }

    static <T> T ignore() {
        return null;
    }

    static UnsupportedOperationException unsupported(Throwable cause, String msg, Object... args) {
        return new UnsupportedOperationException(format(msg, args), cause);
    }

    static UnsupportedOperationException unsupported(String msg, Object... args) {
        return new UnsupportedOperationException(format(msg, args));
    }

    static <T> T unreachableButCompilerNeedsThis() {
        throw new AssertionError("this code should never be reached");
    }

    static String asString(Object result) {
        if (result == null) return null;
        return String.valueOf(result);
    }

    static long now() {
        return timeProvider.getTime();
    }

    static File fileFromURI(URI uri) {
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            String path = uri.getSchemeSpecificPart();
            try {
                path = decode(path, "utf-8");
                return new File(path);
            } catch (UnsupportedEncodingException e) {
                return unreachableButCompilerNeedsThis(/* utf-8 is supported in jre libraries */);
            }
        } else if ("jar".equalsIgnoreCase(uri.getScheme())) {
            String path = uri.getSchemeSpecificPart();
            try {
                return fileFromURI(path.substring(0, path.indexOf('!')));
            } catch (URISyntaxException e) {
                return ignore(/* non critical */);
            }
        }
        return null;
    }

    static File fileFromURI(String uriSpec) throws URISyntaxException {
        try {
            return fileFromURI(new URI(uriSpec));
        } catch (URISyntaxException e) {
            // Perhaps the path contains backslashes
            uriSpec = uriSpec.replace('\\', '/');
            return fileFromURI(new URI(uriSpec));
        }
    }

    static boolean eq(Object o1, Object o2) {
        return o1 == o2 || o1 != null && o1.equals(o2);
    }

    static SystemProvider system() {
        return system;
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw unsupported(e,
                    "Class '%s' cannot be instantiated; see the cause below in the stack trace",
                    clazz.getCanonicalName());
        }
    }

    public static <T> List<T> newInstance(Class<? extends T>[] classes, List<T> result) {
        for (Class<? extends T> clazz : classes)
            result.add(newInstance(clazz));
        return result;
    }
}
