package net.dreamlu.easy.commons.upload;

import java.io.File;
import java.io.IOException;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import net.dreamlu.easy.commons.utils.StrUtils;

/**
 * 将文件重命名为“时间戳_6位随机.ext”的形式
 * 
 * @author L.cm
 */
public class EasyFileRenamePolicy implements FileRenamePolicy {

    @Override
    public File rename(File f) {
        String name = f.getName();
        String ext = null;
        
        int dot = name.lastIndexOf(".");
        if (dot != -1) {
            ext = name.substring(dot);  // includes "."
        } else {
            ext = "";
        }
        long currentMillis = System.currentTimeMillis();
        String random = StrUtils.random(6);
        
        String newName = new StringBuilder(20)
                .append(currentMillis)
                .append('_')
                .append(random)
                .append(ext)
                .toString();
        
        String dir = f.getParent();
        f = new File(dir, newName);
        
        // Increase the count until an empty spot is found.
        // Max out at 9999 to avoid an infinite loop caused by a persistent
        // IOException, like when the destination dir becomes non-writable.
        // We don't pass the exception up because our job is just to rename,
        // and the caller will hit any IOException in normal processing.
        int count = 0;
        while (!createNewFile(f) && count < 9999) {
          count++;
          String _newName = newName + count + ext;
          f = new File(f.getParent(), _newName);
        }
        
        return f;
    }

    private boolean createNewFile(File f) {
        try {
            return f.createNewFile();
        } catch (IOException ignored) {
            return false;
        }
    }

}