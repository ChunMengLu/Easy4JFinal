package net.dreamlu.easy.commons.config;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import com.jfinal.config.Constants;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Prop;
import com.jfinal.kit.StrKit;
import com.jfinal.render.ViewType;

/**
 * 参数转换处理
 * @author L.cm
 */
class ConfigParser {

    /**
     * 该部分代码太多生硬
     */
    public static void parser(Prop prop, Constants me) {
        // 第一步对 key 按照前缀排序，将属性写到TreeMap
        Properties props = prop.getProperties();
        SortedMap<String, String> propMap = new TreeMap<String, String>();
        for (Object object : props.keySet()) {
            String key = (String) object;
            propMap.put(key, prop.get(key));
        }
        
        String encoding     = prop.get("app.encoding", "UTF-8");
        Integer maxPostSize = prop.getInt("app.max-post-size");
        
        String viewType     = prop.get("view.type");
        String viewPrefix   = prop.get("view.prefix");
        
        String baseDownloadPath = prop.get("base.download-path");
        String baseUploadPath   = prop.get("base.upload-path");
        
        String jsonDatePattern  = prop.get("json.date-pattern");
        
        if (StrKit.notBlank(encoding)) {
            me.setEncoding(encoding);
        }
        if (StrKit.notBlank(viewType)) {
            viewType = viewType.toUpperCase();
            if ("beetl".equalsIgnoreCase(viewType)) {
                me.setMainRenderFactory(new org.beetl.ext.jfinal.BeetlRenderFactory());
                if (StrKit.notBlank(viewPrefix)) {
                    org.beetl.core.Configuration config = org.beetl.ext.jfinal.BeetlRenderFactory.groupTemplate.getConf();
                    Map<String, String> res = config.getResourceMap();
                    res.put("root", viewPrefix);
                }
            } else {
                if (StrKit.notBlank(viewPrefix)) {
                    me.setBaseViewPath(viewPrefix);
                }
                me.setViewType(ViewType.valueOf(viewType));
            }
        }
        
        if (null != maxPostSize) {
            me.setMaxPostSize(maxPostSize);
        }
        
        if (StrKit.notBlank(baseDownloadPath)) {
            me.setBaseDownloadPath(baseDownloadPath);
        }
        
        if (StrKit.notBlank(baseUploadPath)) {
            me.setBaseUploadPath(baseUploadPath);
        }
        
        if (StrKit.notBlank(jsonDatePattern)) {
            me.setJsonDatePattern(jsonDatePattern);
        }
        String userKey    = prop.get("user.key");
        String userSecret = prop.get("user.secret");
        
        String devUrlPrefix = prop.get("dev.urlPrefix");
        String devDir = prop.get("dev.devDir");
        
        int sessionTimeout = prop.getInt("session.timeout", 30);
        String sessionCookieName = prop.get("session.cookie.name", "easy_session");
        String sessionCookieDomain = prop.get(" session.cookie.domain");
        
        EasyConstants easyConst = EasyConstants.me;
    }
    
}
