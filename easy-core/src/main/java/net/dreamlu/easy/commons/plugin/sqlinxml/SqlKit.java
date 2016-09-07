package net.dreamlu.easy.commons.plugin.sqlinxml;

import java.util.Map;

/**
 * sql 工具扩展
 * @author L.cm
 */
public class SqlKit {
    private static Map<String, String> sqlMap;

    static void init(Map<String, String> sqlMap) {
        SqlKit.sqlMap = sqlMap;
    }
    
    /**
     * 获取sql
     * @param sqlKey sqlKey
     * @return sql
     */
    public static String get(String sqlKey) {
        return sqlMap.get(sqlKey);
    }
    
    /**
     * 获取sql
     * @param sqlKey sqlKey
     * @return sql
     */
    public static String getSelect(String sqlKey) {
        return sqlMap.get(sqlKey + "@select");
    }
    
    /**
     * 获取sql
     * @param sqlKey sqlKey
     * @return sql
     */
    public static String getExt(String sqlKey) {
        return sqlMap.get(sqlKey + "@ext");
    }
    
}
