package net.dreamlu.easy.commons.searcher;

/**
 * 类查找器
 * @author Dreamlu
 */
public class ClassSearcher extends FileSearcher {

    @Override
    protected boolean visitSystemDirEntry(SystemFileEntry dir) {
        return super.visitSystemDirEntry(dir);
    }

    @Override
    protected void visitSystemFileEntry(SystemFileEntry file) {
        super.visitSystemFileEntry(file);
    }

    @Override
    protected void visitZipDirEntry(ZipFileEntry dir) {
        super.visitZipDirEntry(dir);
    }

    @Override
    protected void visitZipFileEntry(ZipFileEntry file) {
        super.visitZipFileEntry(file);
    }

    @Override
    protected boolean visitDirEntry(FileEntry dir) {
        return super.visitDirEntry(dir);
    }

    @Override
    protected void visitFileEntry(FileEntry file) {
        super.visitFileEntry(file);
    }
}
