package net.dreamlu.easy.commons.plugin.sqlinxml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.IPlugin;

import net.dreamlu.easy.commons.utils.FileUtils;
import net.dreamlu.easy.commons.utils.SuffixFileFilter;
import net.dreamlu.easy.commons.utils.XmlHelper;

/**
 * 参照JFinal-ext里面的插件名
 * @author Dreamlu
 */
public class SqlInXmlPlugin implements IPlugin {
    private Map<String, String> sqlMap;
    private String[] sqlPath;
    
    public SqlInXmlPlugin() {
        this(new String[]{"sqls"}); //默认扫描classPath下的sqls目录
    }
    
    public SqlInXmlPlugin(String... sqlPath) {
        this.sqlPath = sqlPath;
        sqlMap = new HashMap<String, String>();
    }
    
    @Override
    public boolean start() {
        String classPath = PathKit.getRootClassPath();
        if (null == sqlPath) {
            throw new NullPointerException("sqlPath is null!");
        }
        List<File> xmlList = new ArrayList<File>();
        SuffixFileFilter filter = new SuffixFileFilter(".xml");
        
        // is '/'; on Microsoft Windows systems it is '\\'.
        for (String path : sqlPath) {
            if (!path.startsWith("/") && !path.startsWith("\\")) {
                path = File.separator + path;
            }
            String xmlDir = classPath + path; 
            List<File> xmls = FileUtils.list(xmlDir, filter);
            xmlList.addAll(xmls);
        }
        XmlHelper xmlHelper = null;
        for (File file : xmlList) {
            xmlHelper = XmlHelper.of(file);
            parseSql(xmlHelper);
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
     * 装载并处理sql
     * @param sqlKey sqlKey
     * @param sqlValue sql
     */
    private void put(String sqlKey, String sqlValue) {
        sqlValue = sqlValue.replaceAll("\\s+", " ");
        sqlMap.put(sqlKey, sqlValue);
    }
    
    @Override
    public boolean stop() {
        sqlMap.clear();
        sqlMap = null;
        sqlPath = null;
        return true;
    }

}
