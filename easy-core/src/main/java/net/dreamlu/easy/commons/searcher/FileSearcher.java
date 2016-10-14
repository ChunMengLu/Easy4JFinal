package net.dreamlu.easy.commons.searcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.dreamlu.easy.commons.utils.ClassUtils;
import net.dreamlu.easy.commons.utils.IOUtils;
import net.dreamlu.easy.commons.utils.URLUtils;

/**
 * 文件搜索器
 * 本段代码来自jetbrick-template-1x
 */
public abstract class FileSearcher {
    /**
     * 搜索文件系统.
     */
    public void lookupFileSystem(File dir) {
        doLookupInFileSystem(dir, null, null);
    }
    
    /**
     * 搜索指定 package 下面的文件.
     */
    public void lookupClasspath(String... packageNames) {
        if (packageNames == null || packageNames.length == 0) {
            Collection<URL> urls = ClassUtils.getClasspathURLs(null);
            doGetClasspathResources(urls, null);
        } else {
            for (String pkg : packageNames) {
                Collection<URL> urls = ClassUtils.getClasspathURLs(pkg);
                doGetClasspathResources(urls, pkg.replace('.', '/'));
            }
        }
    }
    
    /**
     * 搜索 jar/zip.
     */
    public void lookupZipFile(File zipFile, String entryName) {
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile);
            doLookupInZipFile(zip, entryName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(zip);
        }
    }
    
    private void doGetClasspathResources(Collection<URL> urls, String pkgdir) {
        for (URL url : urls) {
            String protocol = url.getProtocol();
            
            if ("file".equals(protocol)) {
                File file = URLUtils.toFileObject(url);
                if (file.isDirectory()) {
                    doLookupInFileSystem(file, pkgdir, null);
                } else {
                    String name = file.getName().toLowerCase();
                    if (name.endsWith(".jar") || name.endsWith(".zip")) {
                        doLookupInZipFile(url, pkgdir);
                    }
                }
            } else if ("jar".equals(protocol) || "zip".equals(protocol)) {
                doLookupInZipFile(url, pkgdir);
            } else if ("vfs".equals(protocol)) {
                doLookupInVfsFile(url, pkgdir);
            } else {
                throw new IllegalStateException("Unsupported url format: " + url.toString());
            }
        }
    }
    
    private void doLookupInFileSystem(File dir, String pkgdir, String relativeName) {
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            String name = (relativeName == null) ? file.getName() : relativeName + '/' + file.getName();
            SystemFileEntry entry = new SystemFileEntry(file, pkgdir, name);
            if (file.isDirectory()) {
                if (visitSystemDirEntry(entry)) {
                    doLookupInFileSystem(file, pkgdir, name);
                }
            } else {
                visitSystemFileEntry(entry);
            }
        }
    }
    
    private void doLookupInZipFile(URL url, String pkgdir) {
        ZipFile zip = null;
        try {
            if ("jar".equals(url.getProtocol())) {
                zip = ((JarURLConnection) url.openConnection()).getJarFile();
            } else {
                File file = URLUtils.toFileObject(url);
                if (!file.exists()) {
                    return;
                }
                zip = new ZipFile(file);
            }
            doLookupInZipFile(zip, pkgdir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(zip);
        }
    }
    
    private void doLookupInZipFile(ZipFile zip, String pkgdir) {
        if (pkgdir == null || pkgdir.length() == 0) {
            pkgdir = null;
        } else {
            pkgdir = pkgdir + '/';
        }
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体, 可以是目录和一些jar包里的其他文件 如META-INF等文件
            ZipEntry entry = entries.nextElement();
            String entryName = entry.getName();
            if (entry.isDirectory()) {
                entryName = entryName.substring(0, entryName.length() - 1);
            }
            
            if (pkgdir == null) {
                if (entry.isDirectory()) {
                    visitZipDirEntry(new ZipFileEntry(zip, entry, entryName));
                } else {
                    visitZipFileEntry(new ZipFileEntry(zip, entry, entryName));
                }
            } else if (entryName.startsWith(pkgdir)) {
                entryName = entryName.substring(pkgdir.length());
                if (entry.isDirectory()) {
                    visitZipDirEntry(new ZipFileEntry(zip, entry, entryName));
                } else {
                    visitZipFileEntry(new ZipFileEntry(zip, entry, entryName));
                }
            }
        }
    }
    
    private void doLookupInVfsFile(URL url, String pkgdir) {
        try {
            URLConnection conn = url.openConnection();
            if (conn.getClass().getName().equals("org.jboss.vfs.protocol.VirtualFileURLConnection")) {
                String vfs = conn.getContent().toString(); // VirtualFile
                File file = new File(vfs.substring(1, vfs.length() - 1));
                if (!file.exists()) {
                    return;
                }
                if (file.isDirectory()) {
                    doLookupInFileSystem(file, pkgdir, null);
                } else {
                    String name = file.getName().toLowerCase();
                    if (name.endsWith(".jar") || name.endsWith(".zip")) {
                        ZipFile zip = new ZipFile(file);
                        try {
                            doLookupInZipFile(zip, pkgdir);
                        } finally {
                            IOUtils.closeQuietly(zip);
                        }
                    }
                }
            } else {
                throw new IllegalStateException("Unsupported URL: " + url);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    //----------------------------------------------------------------
    // following visitXXX methods should be overrided by subclass.
    //
    protected boolean visitSystemDirEntry(SystemFileEntry dir) {
        return visitDirEntry(dir);
    }
    
    protected void visitSystemFileEntry(SystemFileEntry file) {
        visitFileEntry(file);
    }
    
    protected void visitZipDirEntry(ZipFileEntry dir) {
        visitDirEntry(dir);
    }
    
    protected void visitZipFileEntry(ZipFileEntry file) {
        visitFileEntry(file);
    }
    
    protected boolean visitDirEntry(FileEntry dir) {
        return true;
    }
    
    protected void visitFileEntry(FileEntry file) {}
    
    //----------------------------------------------------------------
    // innerclass.
    //
    public static interface FileEntry {
        public boolean isDirectory();
        
        public boolean isJavaClass();
        
        public String getName();
        
        public String getRelativePathName();
        
        public String getQualifiedFileName();
        public String getQualifiedJavaName();
        
        public long length();
        
        public long lastModified();
        
        public InputStream getInputStream() throws IOException;
    }
    
    public static class SystemFileEntry implements FileEntry {
        private final File file;
        private final String pkgdir;
        private final String relativeName;
        
        public SystemFileEntry(File file, String pkgdir, String relativeName) {
            this.file = file;
            this.pkgdir = pkgdir;
            this.relativeName = relativeName;
        }
        
        public File getFile() {
            return file;
        }
        
        @Override
        public boolean isDirectory() {
            return file.isDirectory();
        }
        
        @Override
        public boolean isJavaClass() {
            return !file.isDirectory() && file.getName().endsWith(".class");
        }
        
        @Override
        public String getName() {
            return file.getName();
        }
        
        @Override
        public String getRelativePathName() {
            return relativeName;
        }
        
        @Override
        public String getQualifiedFileName() {
            String name;
            if (pkgdir != null) {
                name = pkgdir + '/' + relativeName;
            } else {
                name = relativeName;
            }
            if (file.isDirectory()) {
                return name.replace('/', '.');
            }
            return name;
        }
        
        @Override
        public String getQualifiedJavaName() {
            String name = getQualifiedFileName();
            if (file.isDirectory()) {
                return name;
            } else {
                if (name.endsWith(".class")) {
                    return name.substring(0, name.length() - 6).replace('/', '.');
                }
                throw new IllegalStateException("FileEntry is not a Java Class: " + toString());
            }
        }
        
        @Override
        public long length() {
            return file.length();
        }
        
        @Override
        public long lastModified() {
            return file.lastModified();
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            return new FileInputStream(file);
        }
        
        @Override
        public String toString() {
            return file.toString();
        }
    }
    
    public static class ZipFileEntry implements FileEntry {
        private final ZipFile zip;
        private final ZipEntry entry;
        private final String relativeName;
        
        public ZipFileEntry(ZipFile zip, ZipEntry entry, String relativeName) {
            this.zip = zip;
            this.entry = entry;
            this.relativeName = relativeName;
        }
        
        public ZipFile getZipFile() {
            return zip;
        }
        
        public ZipEntry getZipEntry() {
            return entry;
        }
        
        @Override
        public boolean isDirectory() {
            return entry.isDirectory();
        }
        
        @Override
        public boolean isJavaClass() {
            return entry.getName().endsWith(".class");
        }
        
        @Override
        public String getName() {
            int ipos = relativeName.lastIndexOf('/');
            return ipos != -1 ? relativeName.substring(ipos + 1) : relativeName;
        }
        
        @Override
        public String getRelativePathName() {
            return relativeName;
        }
        
        @Override
        public String getQualifiedFileName() {
            String name = entry.getName();
            if (entry.isDirectory()) {
                return name.substring(0, name.length() - 1).replace('/', '.');
            }
            return name;
        }
        
        @Override
        public String getQualifiedJavaName() {
            String name = getQualifiedFileName();
            if (entry.isDirectory()) {
                return name;
            } else {
                if (name.endsWith(".class")) {
                    return name.substring(0, name.length() - 6).replace('/', '.');
                }
                throw new IllegalStateException("FileEntry is not a Java Class: " + toString());
            }
        }
        
        @Override
        public long length() {
            return entry.getSize();
        }
        
        @Override
        public long lastModified() {
            return entry.getTime();
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            return zip.getInputStream(entry);
        }
        
        @Override
        public String toString() {
            return entry.toString();
        }
    }
}
