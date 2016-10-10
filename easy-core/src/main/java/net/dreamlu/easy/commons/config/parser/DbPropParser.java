package net.dreamlu.easy.commons.config.parser;

/**
 * 数据库连接池配置文件解析
 * @author Dreamlu
 */
public class DbPropParser extends AbstractPropParser {

    public DbPropParser() {
        super("db");
    }

    /**
     * db先检查开启状态
     */
    @Override
    public void parser() {
        String xx = "db.test.enable";
        System.out.println(xx.replaceAll("app\\.(.*)\\.enable", "$1"));
    }

}
