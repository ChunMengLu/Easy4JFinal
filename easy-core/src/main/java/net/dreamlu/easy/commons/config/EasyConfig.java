package net.dreamlu.easy.commons.config;

import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Sqls;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;

import net.dreamlu.controller.UeditorApiController;
import net.dreamlu.easy.commons.interceptors.InjectInterceptor;
import net.dreamlu.easy.commons.logs.Log4j2LogFactory;
import net.dreamlu.easy.commons.plugin.event.EventPlugin;
import net.dreamlu.easy.commons.plugin.ioc.IocPlugin;
import net.dreamlu.easy.commons.plugin.sqlinxml.SqlInXmlPlugin;
import net.dreamlu.easy.commons.servlet.ServletContextInterceptor;
import net.dreamlu.easy.commons.session.SessionHandler;
import net.dreamlu.easy.handler.RenderingTimeHandler;
import net.dreamlu.easy.handler.SessionIdHandler;
import net.dreamlu.easy.handler.ViewDevHandler;
import net.dreamlu.easy.model._MappingKit;

/**
 * Created by L.cm on 2016/5/18.
 */
public abstract class EasyConfig extends JFinalConfig {
    private static final Easy4JFinal esay = Easy4JFinal.me();
    // 配置文件解析
    private ApplicationConfig cfg;
    private Routes routes;
    
    @Override
    public void configConstant(Constants me) {
        esay.initCfg();
        cfg = esay.getCfg();
        me.setDevMode(cfg.devMode());
        // 默认Log4j2日志
        me.setLogFactory(new Log4j2LogFactory());
        // 默认fastJson
        me.setJsonFactory(new FastJsonFactory());
        // 默认Beetl
        me.setMainRenderFactory(new BeetlRenderFactory());
    }

    @Override
    public void configRoute(Routes me) {
        routes = me;
        me.add("/ueditor/api", UeditorApiController.class);
    }

    @Override
    public void configHandler(Handlers me) {
        if (cfg.devMode()) {
            me.add(new RenderingTimeHandler());
            me.add(new ViewDevHandler(cfg.devUrlPrefix(), cfg.devDevDir()));
        }
        me.add(new UrlSkipHandler("/(static|ws)+.*", false));
        me.add(new DruidStatViewHandler("/admin/druid"));
        me.add(new SessionIdHandler());
        if (cfg.sessionEnable()) {
            me.add(new SessionHandler());
        }
    }

    @Override
    public void configInterceptor(Interceptors me) {
        // 全局json异常处理，该类有bug
//        me.addGlobalActionInterceptor(new JsonExceptionInterceptor());''
        // ServletContext拦截器，将request, response存储于ThreadLocal中解耦
        me.addGlobalActionInterceptor(new ServletContextInterceptor());
        me.addGlobalActionInterceptor(new InjectInterceptor());
    }

    @Override
    public void configPlugin(Plugins me) {
        // 数据库信息
        String url      = cfg.dbDefaultUrl();
        String user     = cfg.dbDefaultUser();
        String password = cfg.dbDefaultPwd();
        String driver   = cfg.dbDefaultDriver();
        
        // default 配置Druid数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(url, user, password);
        if (StrKit.notBlank(driver)) {
            druidPlugin.setDriverClass(driver);
        }
        druidPlugin.addFilter(new StatFilter())
                   .addFilter(new Log4j2Filter())
                   .addFilter(new WallFilter());
        me.add(druidPlugin);
        
        // default 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin("main", druidPlugin);
        
        _MappingKit.mapping(arp);
        this.mapping(arp);
        
        arp.setShowSql(cfg.devMode());
        me.add(arp);
        
        me.add(new IocPlugin(routes, cfg.iocScanPkg()));
        
        // ehcahce插件配置
        me.add(new EhCachePlugin());
        
        String[] eventPkg = cfg.eventPkg();
        if (null != eventPkg && eventPkg.length > 0) {
            me.add(new EventPlugin(eventPkg));
        }
        String[] xmlSqlPkg = cfg.xmlSqlPkg();
        if (null != xmlSqlPkg && xmlSqlPkg.length > 0) {
            me.add(new SqlInXmlPlugin(xmlSqlPkg));
        }
        
        String sqlsFile = cfg.sqlsFile();
        if (StrKit.notBlank(sqlsFile)) {
            Sqls.load(sqlsFile);
        }
        
        this.plugin(me);
    }

    @Override
    public void afterJFinalStart() {
        esay.initAfterStart();
        // 启动
        onEasyStart();
        // showBanner，未来再做强化，支持自定义和彩色显示
        showBanner();
    }

    public abstract void constant(Constants me);
    public abstract void mapping(ActiveRecordPlugin arp);
    public abstract void plugin(Plugins plugins);
    public abstract void onEasyStart();

    private void showBanner() {
        System.err.println("Easy4JFinal " + Easy4JFinal.VERSION + " started to complete~~~");
    }
}
