package net.dreamlu.easy.commons.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * IOUtils
 * @author L.cm
 */
public abstract class IOUtils {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    /**
     * closeQuietly
     * @param closeable 自动关闭
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
    
    /**
     * InputStream to String utf-8
     *
     * @param input  the <code>InputStream</code> to read from
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     */
    public static String toString(InputStream input) throws IOException {
        return toString(input, Charsets.UTF_8);
    }

    /**
     * InputStream to String
     *
     * @param input  the <code>InputStream</code> to read from
     * @param charset  the <code>Charset</code>
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException if an I/O error occurs
     */
    public static String toString(InputStream input, Charset charset) throws IOException {
        InputStreamReader in = new InputStreamReader(input, charset);
        StringBuilder out = new StringBuilder();
        char[] c = new char[DEFAULT_BUFFER_SIZE];
        for (int n; (n = in.read(c)) != -1;) {
            out.append(new String(c, 0, n));
        }
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(input);
        return out.toString();
    }

}
