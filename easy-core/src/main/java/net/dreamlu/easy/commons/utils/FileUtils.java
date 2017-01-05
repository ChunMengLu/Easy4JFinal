package net.dreamlu.easy.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * @author L.cm
 */
public abstract class FileUtils {
    /**
     * 默认为true
     * @author L.cm
     */
    public static class TrueFilter implements FilenameFilter, Serializable {
        private static final long serialVersionUID = -6420452043795072619L;
        
        public final static TrueFilter TRUE = new TrueFilter();
        
        @Override
        public boolean accept(File dir, String name) {
            return true;
        }
    }
    
    /**
     * 扫描目录下的文件
     * @param path 路径
     * @return 文件集合
     */
    public static List<File> list(String path) {
        File file = new File(path);
        return list(file, TrueFilter.TRUE);
    }
    
    /**
     * 扫描目录下的文件
     * @param path 路径
     * @param filter 文件名过滤
     * @return 文件集合
     */
    public static List<File> list(String path, FilenameFilter filter) {
        File file = new File(path);
        return list(file, filter);
    }
    
    /**
     * 扫描目录下的文件
     * @param path 路径
     * @return 文件集合
     */
    public static List<File> list(File file) {
        List<File> fileList = new ArrayList<File>();
        return list(file, fileList, TrueFilter.TRUE);
    }
    
    /**
     * 扫描目录下的文件
     * @param path 路径
     * @param filter 文件名过滤
     * @return 文件集合
     */
    public static List<File> list(File file, FilenameFilter filter) {
        List<File> fileList = new ArrayList<File>();
        return list(file, fileList, filter);
    }
    
    /**
     * 扫描目录下的文件
     * @param path 路径
     * @param filter 文件名过滤
     * @return 文件集合
     */
    private static List<File> list(File file, List<File> fileList, FilenameFilter filter) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File _file : files) {
                list(_file, fileList, filter);
            }
        } else {
            // 过滤文件
            File dir = file.getParentFile();
            String name = file.getName();
            boolean accept = filter.accept(dir, name);
            if (file.exists() && accept) {
                fileList.add(file);
            }
        }
        return fileList;
    }
    
    /**
     * Returns the path to the system temporary directory.
     * @return the path to the system temporary directory.
     */
    public static String getTempDir() {
        return System.getProperty("java.io.tmpdir");
    }
    
    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file     the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     * @throws IOException in case of an I/O error
     */
    public static String readToString(final File file) throws IOException {
        return readToString(file, Charsets.UTF_8);
    }
    
    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file     the file to read, must not be {@code null}
     * @param encoding the encoding to use, {@code null} means platform default
     * @return the file contents, never {@code null}
     * @throws IOException in case of an I/O error
     */
    public static String readToString(final File file, final Charset encoding) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            return IOUtils.toString(in, encoding);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    
    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file     the file to write
     * @param data     the content to write to the file
     * @throws IOException in case of an I/O error
     */
    public static void writeToFile(final File file, final String data)
            throws IOException {
        writeToFile(file, data, Charsets.UTF_8, false);
    }
    
    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file     the file to write
     * @param data     the content to write to the file
     * @param append   if {@code true}, then the String will be added to the
     *                 end of the file rather than overwriting
     * @throws IOException in case of an I/O error
     */
    public static void writeToFile(final File file, final String data, final boolean
            append) throws IOException {
        writeToFile(file, data, Charsets.UTF_8, append);
    }
    
    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file     the file to write
     * @param data     the content to write to the file
     * @param encoding the encoding to use, {@code null} means platform default
     * @throws IOException in case of an I/O error
     */
    public static void writeToFile(final File file, final String data, final Charset encoding)
            throws IOException {
        writeToFile(file, data, encoding, false);
    }
    
    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file     the file to write
     * @param data     the content to write to the file
     * @param encoding the encoding to use, {@code null} means platform default
     * @param append   if {@code true}, then the String will be added to the
     *                 end of the file rather than overwriting
     * @throws IOException in case of an I/O error
     */
    public static void writeToFile(final File file, final String data, final Charset encoding, final boolean
            append) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file, append);
            IOUtils.write(data, out, encoding);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}
