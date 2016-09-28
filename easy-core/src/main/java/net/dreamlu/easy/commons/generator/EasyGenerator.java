package net.dreamlu.easy.commons.generator;

import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Created by L.cm on 2016/7/5.
 */
public class EasyGenerator extends Generator {
    public EasyGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir) {
        super(dataSource, new EasyBaseModelGenerator(baseModelPackageName, baseModelOutputDir), new EasyModelGenerator(modelPackageName, baseModelPackageName, modelOutputDir));
    }

    public EasyGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir) {
        super(dataSource, new EasyBaseModelGenerator(baseModelPackageName, baseModelOutputDir));
    }
    
    // 页面模板生成器
    protected TemplateGenerator templateGenerator;
    // 模版目录
    protected String templateDir;
    // 输出目录
    protected String outPutDir;
    
    // 自定义模版变量
    protected Map<String, Object> vars = new HashMap<String, Object>();
    
    public void setTemplateDir(String dir) {
        this.templateDir = dir;
    }
    
    public void setOutPutDir(String outPutDir) {
        this.outPutDir = outPutDir;
    }
    
    public EasyGenerator addVar(String key, Object value) {
        vars.put(key, value);
        return this;
    }
    
    @Override
    public void generate() {
        // 先执行父类的生成
        super.generate();
        
        long start = System.currentTimeMillis();
        List<TableMeta> tableMetas = metaBuilder.build();
        baseModelGenerator.generate(tableMetas);
        
        if (templateGenerator != null) {
            templateGenerator.generate(templateDir, outPutDir, vars, tableMetas);
        }
        
        long usedTime = (System.currentTimeMillis() - start) / 1000;
        System.out.println("EasyGenerator complete in " + usedTime + " seconds.");
    }
}
