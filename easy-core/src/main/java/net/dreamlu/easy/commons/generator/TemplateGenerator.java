package net.dreamlu.easy.commons.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beetl.core.BeetlKit;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import net.dreamlu.easy.commons.utils.Charsets;
import net.dreamlu.easy.commons.utils.DateUtils;
import net.dreamlu.easy.commons.utils.FileUtils;
import net.dreamlu.easy.commons.utils.IOUtils;

/**
 * 模版生成，采用Beetl，用来生成页面、模版等
 * @author L.cm
 */
public class TemplateGenerator {
    /**
     * 模版生成
     * @param templateDir 模版目录
     * @param outPutDir 输出目录
     * @param vars 参数变量
     * @param tableMetas 数据库参数
     */
    public void generate(String templateDir, String outPutDir, Map<String, Object> vars, List<TableMeta> tableMetas) {
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("tableList", tableMetas);
        paras.putAll(vars);
        // 各种变量
        String author = System.getProperty("user.name");
        paras.put("author", author);
        String dateTime = DateUtils.format(new Date(), DateUtils.PATTERN_DATETIME);
        paras.put("dateTime", dateTime);
        // 判断是否绝对路径
        boolean isAbsolutelyPath = PathKit.isAbsolutelyPath(templateDir);
        String templatePath = templateDir;
        if (!isAbsolutelyPath) {
            templatePath = PathKit.getWebRootPath() + "/WEB-INF/generate";
        }
        // 模版目录
        List<File> fileList = FileUtils.list(templatePath);
    }
    
    public static void main(String[] args) {
        
        System.out.println();
    }
    
    /**
     * 根据具体模板生成文件
     * @param templateFileName 模板文件名称
     * @param paras 模版参数
     * @param filePath 输出目录
     */
    private static void generateTemplete(String templateFileName, Map<String, Object> paras, String filePath) {
        FileOutputStream output = null;
        try {
            String data = BeetlKit.render(templateFileName, paras);
            File file = new File(filePath);
            File dir = new File(file.getParent());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            output = new FileOutputStream(file);
            output.write(data.getBytes(Charsets.UTF_8));
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(output);
        }
    }
}
