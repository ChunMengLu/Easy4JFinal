package net.dreamlu.easy.commons.searcher;

import java.util.LinkedHashSet;
import java.util.Set;

import net.dreamlu.easy.commons.searcher.FileSearcher.FileEntry;
import net.dreamlu.easy.commons.utils.Matcher;

/**
 * 文件查找查找器
 * 从classpath或者jar中查找
 * @author L.cm
 */
public class ResourceMatcherSearcher {
    /**
     * 获取文件
     * @param packageName 包名数组
     * @param pattern 文件匹配 支持*和?
     * @return 文件集合
     */
    public static Set<String> getFiles(String[] packageNames, final String pattern) {
        String[] patterns = new String[]{pattern};
        return getFiles(packageNames, patterns);
    }
    
    /**
     * 获取文件
     * @param packageNames 包名数组
     * @param patterns 文件匹配 支持*和?
     * @return 文件集合
     */
    public static Set<String> getFiles(String[] packageNames, final String[] patterns) {
        final Set<String> fileSet = new LinkedHashSet<String>();
        
        FileSearcher finder = new FileSearcher() {
            
            @Override
            public void visitFileEntry(FileEntry file) {
                if (isMatch(file, patterns)) {
                    String fileName = file.getQualifiedFileName();
                    fileSet.add(fileName);
                }
            }
            
        };
        
        finder.lookupClasspath(packageNames);
        
        return fileSet;
    }

    private static boolean isMatch(FileEntry file, String[] patterns) {
        return !file.isDirectory() && Matcher.matchName(file.getName(), patterns);
    }

}
