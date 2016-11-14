package net.dreamlu.easy.commons.config;

import java.util.Date;

import javax.servlet.ServletContext;

import org.aeonbits.owner.ConfigFactory;
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
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.upload.OreillyCos;

import net.dreamlu.controller.UeditorApiController;
import net.dreamlu.easy.commons.core.EasyConst;
import net.dreamlu.easy.commons.interceptors.JsonExceptionInterceptor;
import net.dreamlu.easy.commons.logs.Log4j2LogFactory;
import net.dreamlu.easy.commons.logs.LogPrintStream;
import net.dreamlu.easy.commons.plugin.event.EventPlugin;
import net.dreamlu.easy.commons.plugin.sqlinxml.SqlInXmlPlugin;
import net.dreamlu.easy.commons.servlet.ServletContextInterceptor;
import net.dreamlu.easy.commons.session.SessionHandler;
import net.dreamlu.easy.commons.upload.EasyFileRenamePolicy;
import net.dreamlu.easy.commons.utils.WebUtils;
import net.dreamlu.easy.core.CaptchaController;
import net.dreamlu.easy.core.auth.AuthController;
import net.dreamlu.easy.handler.RenderingTimeHandler;
import net.dreamlu.easy.handler.SessionIdHandler;
import net.dreamlu.easy.handler.ViewDevHandler;
import net.dreamlu.easy.model._MappingKit;

/**
 * Created by L.cm on 2016/5/18.
 */
public abstract class EasyConfig extends JFinalConfig {
    // 配置文件解析
    ApplicationConfig cfg = ConfigFactory.create(ApplicationConfig.class);

    @Override
    public void configConstant(Constants me) {
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
        me.add("/captcha", CaptchaController.class);
        me.add("/auth", AuthController.class);
        me.add("/ueditor/api", UeditorApiController.class);
        this.route(me);
    }

    @Override
    public void configHandler(Handlers me) {
        if (cfg.devMode()) {
            me.add(new RenderingTimeHandler());
            me.add(new ViewDevHandler(cfg.devUrlPrefix(), cfg.devDevDir()));
        }
        me.add(new UrlSkipHandler("/static", false));
        me.add(new UrlSkipHandler("/ws", false));
        me.add(new DruidStatViewHandler("/admin/druid"));
        me.add(new SessionIdHandler());
        if (cfg.sessionEnable()) {
            me.add(new SessionHandler());
        }
    }

    @Override
    public void configInterceptor(Interceptors me) {
        // 全局json异常处理
        me.addGlobalActionInterceptor(new JsonExceptionInterceptor());
        // ServletContext拦截器，将request, response存储于ThreadLocal中解耦
        me.addGlobalActionInterceptor(new ServletContextInterceptor());
    }

    @Override
    public void configPlugin(Plugins me) {
        // 数据库信息
        String url      = cfg.dbDefaultUrl();
        String user     = cfg.dbDefaultUser();
        String password = cfg.dbDefaultPwd();
        
        // default 配置Druid数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(url, user, password);
        druidPlugin.setDriverClass("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
        druidPlugin.addFilter(new StatFilter()).addFilter(new Log4j2Filter());
        WallFilter wall = new WallFilter();
        druidPlugin.addFilter(wall);
        me.add(druidPlugin);
        
        // default 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin("default", druidPlugin);
        
        _MappingKit.mapping(arp);
        this.mapping(arp);
        
        arp.setShowSql(cfg.devMode());
        me.add(arp);
        
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
        
        this.plugin(me);
    }

    @Override
    public void afterJFinalStart() {
        // 正式环境,将System.out、err输出到log中
        if (!cfg.devMode()) {
            System.setOut(new LogPrintStream(false));
            System.setErr(new LogPrintStream(true));
        }
        
        // 用户登陆是使用的cookie name和密钥
        WebUtils.setUserKey(cfg.userKey());
        WebUtils.setUserSecret(cfg.userSecret());
        
        // 在JFinal启动时，加入启动时间 ${startTime}
        JFinal jfinal = JFinal.me();
        ServletContext servletContext = jfinal.getServletContext();
        servletContext.setAttribute("startTime", new Date());
        // ctxPath
        String ctxPath = jfinal.getContextPath();
        servletContext.setAttribute("ctxPath", ctxPath);
        // 静态文件目录
        servletContext.setAttribute("stcPath", ctxPath + "/static");
        
        // 修改默认的重命名策略
        OreillyCos.setFileRenamePolicy(new EasyFileRenamePolicy());
        
        // 启动
        onEasyStart();
        
        // showBanner，未来再做强化，支持自定义和彩色显示
        showBanner();
    }

    public abstract void constant(Constants me);
    public abstract void route(Routes me);
    public abstract void mapping(ActiveRecordPlugin arp);
    public abstract void plugin(Plugins plugins);
    public abstract void onEasyStart();

    private void showBanner() {
        System.err.println("Easy4JFinal " + EasyConst.EASY4JFINAL_VERSION + " started to complete~~~");
    }
}
