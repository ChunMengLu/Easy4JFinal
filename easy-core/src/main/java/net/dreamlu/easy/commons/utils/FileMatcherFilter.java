package net.dreamlu.easy.commons.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;

/**
 * 文件后缀过滤
 * @author L.cm
 */
public class FileMatcherFilter implements FilenameFilter, Serializable{
    private static final long serialVersionUID = 812598009067554612L;
    
    private final String pattern;
    
    public FileMatcherFilter(String pattern) {
        this.pattern = pattern;
    }
    
    @Override
    public boolean accept(File dir, String name) {
        return Matcher.matchName(name, pattern);
    }
    
}
