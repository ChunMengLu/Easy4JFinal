package net.dreamlu.easy.commons.config.parser;

public class AppPropParser extends AbstractPropParser {

    /**
     * 前缀app
     */
    public AppPropParser() {
        super("app");
    }

    /**
     * app为默认，忽略开启状态，直接解析
     */
    @Override
    public void parser() {
        
    }

}
