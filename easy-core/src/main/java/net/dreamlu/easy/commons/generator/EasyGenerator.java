package net.dreamlu.easy.commons.generator;

import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import java.util.List;

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
    
    @Override
    public void generate() {
        // 先执行父类的生成
        super.generate();
        
        long start = System.currentTimeMillis();
        List<TableMeta> tableMetas = metaBuilder.build();
        baseModelGenerator.generate(tableMetas);
        
        if (templateGenerator != null) {
            templateGenerator.generate(tableMetas);
        }
        
        long usedTime = (System.currentTimeMillis() - start) / 1000;
        System.out.println("EasyGenerator complete in " + usedTime + " seconds.");
    }
}
