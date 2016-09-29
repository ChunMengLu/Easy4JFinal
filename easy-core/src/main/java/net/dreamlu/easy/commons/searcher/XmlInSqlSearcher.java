package net.dreamlu.easy.commons.searcher;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * xml sql 查找器
 * @author Dreamlu
 */
public class XmlInSqlSearcher {

    public static Set<File> getXml(List<String> packageNames, boolean recursive) {
        final Set<File> xmlFile = new LinkedHashSet<File>();
        
        FileSearcher finder = new FileSearcher() {
            @Override
            public void visitFileEntry(FileEntry file) {
                if (file.isXml()) {
                    addXmlFile(file.getRelativePathName());
                }
            }

            @Override
            protected void visitZipFileEntry(ZipFileEntry file) {
                if (file.isXml()) {
                    addXmlFile(file.getRelativePathName());
                }
            }

            private void addXmlFile(String relativePathName) {
                xmlFile.add(new File(relativePathName));
            }
        };
        
        finder.lookupClasspath(packageNames, recursive);
        
        return xmlFile;
    }

    public static void main(String[] args) {
        Set<File> set = XmlInSqlSearcher.getXml(Arrays.asList("net"), true);
        System.out.println(set);
    }
}
