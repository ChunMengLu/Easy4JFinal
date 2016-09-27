package net.dreamlu.easy.commons.parsing;

import java.util.Map;

/**
 * 属性处理器
 * @author Dreamlu
 */
public class PropTokenHandler implements TokenHandler {
    private final Map<String, String> prop;
    
    public PropTokenHandler(Map<String, String> prop) {
        this.prop = prop;
    }

    @Override
    public String handleToken(String content) {
        return prop.get(content);
    }

}
