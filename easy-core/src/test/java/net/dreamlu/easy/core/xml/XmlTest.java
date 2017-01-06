package net.dreamlu.easy.core.xml;

import net.dreamlu.easy.commons.plugin.sqlinxml.SqlInXmlPlugin;
import net.dreamlu.easy.commons.plugin.sqlinxml.SqlKit;

public class XmlTest {
    
    public static void main(String[] args) {
        SqlInXmlPlugin pp = new SqlInXmlPlugin("net");
        
        pp.start();
        
        System.out.println(SqlKit.get("net.dreamlu.model.UserModel#findByName@ext"));
    }
}

