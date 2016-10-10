package net.dreamlu.easy.commons.config.parser;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class PropParserManage {
    private static ConcurrentMap<String, AbstractPropParser> models = new ConcurrentHashMap<String, AbstractPropParser>();
    
    /**
     * 装载解析器
     * @param prefix 解析的前缀
     * @param parser 解析器
     */
    public static void init(String prefix, AbstractPropParser parser) {
        models.put(prefix, parser);
    }
}
