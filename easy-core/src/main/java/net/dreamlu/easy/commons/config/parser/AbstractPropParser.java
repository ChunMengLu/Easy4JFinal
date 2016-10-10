package net.dreamlu.easy.commons.config.parser;

/**
 * 配置文件解析器
 * @author Dreamlu
 */
public abstract class AbstractPropParser {
    protected boolean enable = false;
    protected String prefix;
    
    public AbstractPropParser(String prefix) {
        // 装载解析器
        PropParserManage.init(prefix, this);
    }

    public abstract void parser();

    public boolean isEnable() {
        return enable;
    }

    public String getPrefix() {
        return prefix;
    }

}
