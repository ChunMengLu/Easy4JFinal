package net.dreamlu.easy.commons.upload;

import java.io.File;

import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.OreillyCos;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * 文件上传重命名策略
 * @author L.cm 
 */
public class EasyFileRenamePolicy implements FileRenamePolicy {
    private final String fileRenamePattern;

    public EasyFileRenamePolicy(String fileRenamePattern) {
        this.fileRenamePattern = fileRenamePattern;
    }

    @Override
    public File rename(File f) {
        // JFinal中设置的默认上传目录
        String baseUploadPath = JFinal.me().getConstants().getBaseUploadPath();
        // 修正后的上传的文件的路径
        String uploadPath = EasyFileRenamePolicy.getUploadPath(baseUploadPath);
        // 老文件名
        String fileName = f.getName();
        // FilePathFormat
        String formatedPath = FilePathFormat.parse(fileRenamePattern, fileName);
        // 获取文件名后缀包含“.”
        String fileSuffix = fileName.substring(fileName.lastIndexOf('.'));
        
        // /xxx/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}.txt
        StringBuilder newFileName = new StringBuilder(uploadPath)
            .append(File.separator)
            .append(formatedPath)
            .append(File.separator)
            .append(fileSuffix);
        
        File dest = new File(newFileName.toString());
        // 创建上层目录
        File dir = dest.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dest;
    }

    /**
     * @see OreillyCos#doInit(String uploadPath, int maxPostSize, String encoding)
     * @param uploadPath 用户设置的base上传路径
     * @return 修正后的上传路径
     */
    private static String getUploadPath(String uploadPath) {
        uploadPath = uploadPath.trim();
        // replaceAll 改为 replace
        uploadPath = uploadPath.replace("\\", "/");
        
        String baseUploadPath;
        if (PathKit.isAbsolutelyPath(uploadPath)) {
            baseUploadPath = uploadPath;
        } else {
            baseUploadPath = PathKit.getWebRootPath() + File.separator + uploadPath;
        }
        
        // remove "/" postfix
        if (baseUploadPath.equals("/") == false) {
            if (baseUploadPath.endsWith("/")) {
                baseUploadPath = baseUploadPath.substring(0, baseUploadPath.length() - 1);
            }
        }
        
        return baseUploadPath;
    }

}