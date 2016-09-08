package net.dreamlu.easy.commons.plugin.sqlinxml;

import java.util.Map;

import com.jfinal.plugin.activerecord.CPI;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import net.dreamlu.easy.commons.parsing.TokenHandler;

/**
 * Sql变量处理器
 * 
 * 处理sql中的变量，注意有注入的风险
 * 
 * select * from ${table}
 * 
 * @author L.cm
 */
public class SqlVarHandler implements TokenHandler {
    private final Map<String, Object> vars;
    
    public SqlVarHandler(Map<String, Object> vars) {
        this.vars = vars;
    }

    public SqlVarHandler(Model<?> model) {
        this.vars = CPI.getAttrs(model);
    }
    
    public SqlVarHandler(Record record) {
        this.vars = record.getColumns();
    }
    
    @Override
    public String handleToken(String content) {
        // 去除空格，避免 ${ xx }的情况
        Object object = vars.get(content.trim());
        return converter(object);
    }

    /**
     * sql的变量处理
     * 
     * sql的变量分为数字型，数字型直接填充即可
     * 
     * 字符型的需要填充成 'xx'
     * 
     * 采用默认的类型转换，不知道是否会有其他的问题
     * 
     * @param object 数据
     * @return 字符串
     */
    private String converter(Object object) {
        if (object instanceof Number) {
            return object.toString();
        }
        return "'" + object + "'";
    }

}
