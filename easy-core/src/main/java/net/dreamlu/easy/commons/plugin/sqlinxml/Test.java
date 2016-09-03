package net.dreamlu.easy.commons.plugin.sqlinxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.dreamlu.easy.commons.utils.XmlHelper;

public class Test {

    public static void main(String[] args) throws FileNotFoundException {
        String pathname = "C:\\Users\\Dreamlu\\Desktop\\xml_sql\\xml_sql.xml";
        
        XmlHelper xmlHelper = XmlHelper.of(new FileInputStream(new File(pathname)));
        
        Node mapperNode = xmlHelper.getNode("mapper");
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

                System.out.println(sqlKey);
                
                System.out.println(sqlNode.getTextContent());
                
                System.out.println(sql.replaceAll("\\s+", " "));
                continue;
            }
            if (null != select) {
                String sql = xmlHelper.getString(select, "text()");
                System.out.println(sqlKey + "@select");
                
                System.out.println(select.getTextContent());
                
                System.out.println(sql.replaceAll("\\s+", " "));
            }
            if (null != ext) {
                String sql = xmlHelper.getString(ext, "text()");
                
                System.out.println(sqlKey + "@ext");
                
                System.out.println(ext.getTextContent());
                
                System.out.println(sql.replaceAll("\\s+", " "));
            }
        }
    }
}
