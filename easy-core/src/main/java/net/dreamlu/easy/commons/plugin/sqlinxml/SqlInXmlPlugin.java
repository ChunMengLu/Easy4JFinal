package net.dreamlu.easy.commons.plugin.sqlinxml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jfinal.plugin.IPlugin;

import net.dreamlu.easy.commons.searcher.ResourceMatcherSearcher;
import net.dreamlu.easy.commons.utils.ClassUtils;
import net.dreamlu.easy.commons.utils.IOUtils;
import net.dreamlu.easy.commons.utils.XmlHelper;

/**
 * 默认扫描classPath下的sqls目录
 * 参照JFinal-ext里面的插件名
 * @author L.cm
 */
public class SqlInXmlPlugin implements IPlugin {
    private Map<String, String> sqlMap;
    private String[] sqlPkg;
    
    public SqlInXmlPlugin() {
        this(new String[]{"sqls"});
    }
    
    public SqlInXmlPlugin(String... sqlPkg) {
        this.sqlPkg = sqlPkg;
        sqlMap = new HashMap<String, String>();
    }
    
    @Override
    public boolean start() {
        if (null == sqlPkg) {
            throw new NullPointerException("sqlPkgs is null!");
        }
        // 查找sql.xml文件
        Set<String> xmlSet = ResourceMatcherSearcher.getFiles(sqlPkg, "*sql.xml");
        
        ClassLoader classLoader = ClassUtils.getClassLoader();
        
        XmlHelper xmlHelper = null;
        for (String resource : xmlSet) {
            InputStream is = classLoader.getResourceAsStream(resource);
            xmlHelper = XmlHelper.of(is);
            parseSql(xmlHelper);
            IOUtils.closeQuietly(is);
        }
        SqlKit.init(sqlMap);
        return true;
    }

    /**
     * 解析xml
     * @param xmlHelper xml解析
     */
    private void parseSql(XmlHelper xmlHelper) {
        Node mapperNode = xmlHelper.getNode("mapper");
        if (null == mapperNode) return;
        
        String namespace = xmlHelper.getString(mapperNode, "@namespace");
        NodeList sqlList = xmlHelper.getNodeList(mapperNode, "sql");
        for (int i = 0; i < sqlList.getLength(); i++) {
            Node sqlNode = sqlList.item(i);
            String id = xmlHelper.getString(sqlNode, "@id");
            Node select = xmlHelper.getNode(sqlNode, "select");
            Node ext    = xmlHelper.getNode(sqlNode, "ext");
            
            String sqlKey = namespace + "#" + id;
            
            if (null == select && null == ext) {
                String sql = xmlHelper.getString(sqlNode, "text()");
                put(sqlKey, sql);
                continue;
            }
            if (null != select) {
                String sql = xmlHelper.getString(select, "text()");
                put(sqlKey + "@select", sql);
            }
            if (null != ext) {
                String sql = xmlHelper.getString(ext, "text()");
                put(sqlKey + "@select", sql);
            }
        }
    }
    
    /**
     * 装载并处理sql，减少不必要的不可见字符
     * @param sqlKey sqlKey
     * @param sqlValue sql
     */
    private void put(String sqlKey, String sqlValue) {
        sqlValue = sqlValue.replaceAll("\\s+", " ").trim();
        sqlMap.put(sqlKey, sqlValue);
    }
    
    @Override
    public boolean stop() {
        sqlMap.clear();
        sqlMap = null;
        sqlPkg = null;
        return true;
    }

}
