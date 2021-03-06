package net.dreamlu.easy.commons.generator;

import com.jfinal.plugin.activerecord.generator.BaseModelGenerator;

/**
 * Created by L.cm on 2016/7/5.
 */
public class EasyBaseModelGenerator extends BaseModelGenerator {

    public EasyBaseModelGenerator(String baseModelPackageName, String baseModelOutputDir) {
        super(baseModelPackageName, baseModelOutputDir);

        this.importTemplate =
                "import net.dreamlu.easy.commons.base.EasyModel;%n" +
                "import com.jfinal.plugin.activerecord.IBean;%n%n";

        this.classDefineTemplate =
                "/**%n" +
                " * Generated by Easy4JFinal, do not modify this file.%n" +
                " */%n" +
                "@SuppressWarnings(\"serial\")%n" +
                "public abstract class %s<M extends %s<M>> extends EasyModel<M> implements IBean {%n%n";
    }
}
