package net.dreamlu.easy.commons.generator;

import com.jfinal.plugin.activerecord.generator.Generator;

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
}
