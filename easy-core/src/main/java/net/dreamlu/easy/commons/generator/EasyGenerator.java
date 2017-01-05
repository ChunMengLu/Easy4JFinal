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
    
    // 模版目录
    protected String templateDir;
    // 输出目录
    protected String outPutDir;
    // 是否生成模版
    private boolean generateTemplate = false;
    
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
    
    public void setGenerateTemplate(boolean generateTemplate) {
        this.generateTemplate = generateTemplate;
    }

    @Override
    public void generate() {
        // 先执行父类的生成
        super.generate();
        
        long start = System.currentTimeMillis();
        List<TableMeta> tableMetas = metaBuilder.build();
        baseModelGenerator.generate(tableMetas);
        
        if (generateTemplate) {
            TemplateGenerator.generate(templateDir, outPutDir, vars, tableMetas);
        }
        
        long usedTime = (System.currentTimeMillis() - start) / 1000;
        System.out.println("EasyGenerator template in " + usedTime + " seconds.");
    }
}
