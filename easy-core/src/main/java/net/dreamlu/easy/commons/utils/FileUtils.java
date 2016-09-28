package net.dreamlu.easy.commons.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
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
        }
        // 过滤文件
        File dir = file.getParentFile();
        String name = file.getName();
        boolean accept = filter.accept(dir, name);
        if (file.exists() && accept) {
            fileList.add(file);
        }
        return fileList;
    }

}
