package net.dreamlu.easy.commons.config;

import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.config.Constants;
import com.jfinal.kit.Prop;

import net.dreamlu.easy.commons.logs.Slf4jLogFactory;

/**
 * 参数转换处理
 * @author L.cm
 */
class ConfigParser {

    public static void parser(Prop prop, Constants me) {
        String encoding = prop.get("app.encoding", "UTF-8");
        String viewType = prop.get("app.view.type");
        String viewPrefix = prop.get("app.view.prefix");
        String viewSuffix = prop.get("app.view.suffix");
        
        int maxPostSize = prop.getInt("app.max-post-size");
        int freeMarkerUpdateDelay = prop.getInt("app.free-marker.update-delay");
        
        String log = prop.get("app.log");
        String json = prop.get("app.json");
        
        // 设置Slf4日志
        me.setLogFactory(new Slf4jLogFactory());
        // beetl模版配置工厂
        me.setMainRenderFactory(new BeetlRenderFactory());
        
        EasyConstants easyConst = EasyConstants.me;
    }
    
}
