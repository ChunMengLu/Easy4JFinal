package net.dreamlu.easy.commons.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL相关工具类
 */
public class URLUtils {
    /**
     * URL编码
     * @param url 地址
     * @return {String}
     */
    public static String encode(String url) {
        try {
            return URLEncoder.encode(url, Charsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * URL解码
     * @param url 地址
     * @return {String}
     */
    public static String decode(String url) {
        try {
            return URLDecoder.decode(url, Charsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    // 下面代码来自jetbrick-template-1x
    public static final String FILE_PROTOCOL = "file";
    public static final String JAR_PROTOCOL = "jar";
    public static final String ZIP_PROTOCOL = "zip";
    public static final String VFS_PROTOCOL = "vfs";
    public static final String FILE_PROTOCOL_PREFIX = "file:";
    public static final String JAR_PROTOCOL_PREFIX = "jar:";
    public static final String ZIP_PROTOCOL_PREFIX = "zip:";
    public static final String VFS_PROTOCOL_PREFIX = "vfs:";
    public static final String JAR_FILE_SEPARATOR = "!/";

    public static URL fromFile(String file) {
        return fromFile(new File(file));
    }

    public static URL fromFile(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static File toFileObject(URL url) {
        if (url == null) return null;
        return new File(toFilePath(url));
    }

    public static String toFilePath(URL url) {
        if (url == null) return null;

        String protocol = url.getProtocol();
        String file = url.getPath();
        try {
            file = URLDecoder.decode(file, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }

        if (FILE_PROTOCOL.equals(protocol)) {
            return file;
        } else if (JAR_PROTOCOL.equals(protocol) || ZIP_PROTOCOL.equals(protocol)) {
            int ipos = file.indexOf(JAR_FILE_SEPARATOR);
            if (ipos > 0) {
                file = file.substring(0, ipos);
            }
            if (file.startsWith(FILE_PROTOCOL_PREFIX)) {
                file = file.substring(FILE_PROTOCOL_PREFIX.length());
            }
            return file;
        } else if (VFS_PROTOCOL.equals(protocol)) {
            int ipos = file.indexOf(JAR_FILE_SEPARATOR);
            if (ipos > 0) {
                file = file.substring(0, ipos);
            } else if (file.endsWith("/")) {
                file = file.substring(0, file.length() - 1);
            }
            return file;
        }
        return file;
    }
}
