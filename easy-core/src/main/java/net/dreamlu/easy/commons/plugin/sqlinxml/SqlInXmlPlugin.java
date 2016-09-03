package net.dreamlu.easy.commons.plugin.sqlinxml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.internal.compiler.batch.Main;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.IPlugin;

/**
 * 参照JFinal-ext里面的插件名
 * @author Dreamlu
 */
public class SqlInXmlPlugin implements IPlugin {
    private Map<String, String> sqlMap;
    private String[] sqlPath;
    
    public SqlInXmlPlugin() {
        this(new String[]{"sqls"});
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
        // is '/'; on Microsoft Windows systems it is '\\'.
        for (String path : sqlPath) {
            if (!path.startsWith("/") && !path.startsWith("\\")) {
                path = File.separator + path;
            }
            
            
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(PathKit.getRootClassPath());
    }
    
    @Override
    public boolean stop() {
        sqlMap.clear();
        sqlMap = null;
        sqlPath = null;
        return true;
    }

}
