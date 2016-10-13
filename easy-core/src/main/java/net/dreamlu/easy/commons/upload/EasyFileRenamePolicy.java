package net.dreamlu.easy.commons.upload;

import java.io.File;

import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.OreillyCos;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * 文件上传重命名策略
 * 支持重命名时添加目录规则，由于JFinal2.2中已经不支持重命名时修改目录，故对cos进行了大量修改
 * 
 * jfinal中获取到的UploadFile中的uploadPath,fileName,originalFileName全都不可信
 * 
 * uploadPath：
 * 为你在JFinal中setBaseUploadPath的目录，默认为upload
 * 
 * getOriginalFileName :
 * 如果是上传到项目目录下，将会返回 相对于项目的目录:/upload/image/20161013/1476324491632007019.txt
 * 如果是上传到服务器的其他目录/var/www，将会返回当前的文件全路径
 * 
 * fileName:
 * 将会返回一个相对于uploadPath的路径，如：D:\\easy-example\\src\\main\\webapp\\upload\\image/20161013/1476324491632007019.txt
 * uploadPath默认为upload则返回：/image/20161013/1476324491632007019.txt
 * 
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
            .append(fileSuffix);
        
        System.out.println(newFileName);
        
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
        uploadPath = uploadPath.replace("\\", "/")
                .replace("//", "/");
        
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