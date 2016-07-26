package net.dreamlu.easy.config;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import net.dreamlu.controller.UeditorApiController;
import net.dreamlu.easy.commons.interceptors.JsonExceptionInterceptor;
import net.dreamlu.easy.commons.logs.LogPrintStream;
import net.dreamlu.easy.commons.logs.Slf4jLogFactory;
import net.dreamlu.easy.commons.servlet.ServletContextInterceptor;
import net.dreamlu.easy.commons.utils.WebUtils;
import net.dreamlu.easy.core.CaptchaController;
import net.dreamlu.easy.core.auth.AuthController;
import net.dreamlu.easy.handler.RenderingTimeHandler;
import net.dreamlu.easy.handler.SessionIdHandler;
import net.dreamlu.easy.handler.ViewDevHandler;
import net.dreamlu.easy.model._MappingKit;
import net.dreamlu.easy.ui.beetl.SqlsTag;
import org.beetl.ext.jfinal.BeetlRenderFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by L.cm on 2016/5/18.
 */
public abstract class EasyConfig extends JFinalConfig {
    // 开发模式
    private boolean devMode = false;

    @Override
    public void configConstant(Constants me) {
        loadPropertyFile("application.properties");
        devMode = getPropertyToBoolean("app.devMode", false);
        me.setEncoding(getProperty("app.encoding", "UTF-8"));

        me.setDevMode(devMode);
        // 设置Slf4日志
        me.setLogFactory(new Slf4jLogFactory());
        // beetl模版配置工厂
        BeetlRenderFactory beetlRenderFactory = new BeetlRenderFactory();
        me.setMainRenderFactory(beetlRenderFactory);
        // 加载用户自己的配置
        this.constant(me);
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
        if (devMode) {
            me.add(new RenderingTimeHandler());
            // 路由的开始部分
            String urlPrefix = getProperty("app.dev.urlPrefix", "/_dev");
            // 前端开发的模版目录
            String devDir    = getProperty("app.dev.devDir", "_dev");
            me.add(new ViewDevHandler(urlPrefix, devDir));
        }
        me.add(new UrlSkipHandler("/static", false));
        me.add(new DruidStatViewHandler("/admin/druid"));
        me.add(new SessionIdHandler());
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
        String url      = getProperty("db.default.url");
        String user     = getProperty("db.default.user");
        String password = getProperty("db.default.password");

        // default 配置Druid数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(url, user, password);
        druidPlugin.addFilter(new StatFilter()).addFilter(new Slf4jLogFilter());
        WallFilter wall = new WallFilter();
        druidPlugin.addFilter(wall);
        me.add(druidPlugin);

        // default 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin("default", druidPlugin);

        _MappingKit.mapping(arp);
        this.mapping(arp);

        arp.setShowSql(devMode);
        me.add(arp);

        // ehcahce插件配置
        me.add(new EhCachePlugin());
        this.plugin(me);
    }

    @Override
    public void afterJFinalStart() {
        super.afterJFinalStart();
        // 用户登陆是使用的cookie name和密钥
        String userKey    = getProperty("app.user.key");
        String userSecret = getProperty("app.user.secret");
        WebUtils.setUserKey(userKey);
        WebUtils.setUserSecret(userSecret);

        // 在JFinal启动时，beetl变量中加入启动时间 ${startTime!, "yyyy-MM-dd HH:mm:ss"}
        Map<String, Object> sharedVars = new HashMap<String, Object>();
        sharedVars.put("startTime", new Date());
        BeetlRenderFactory.groupTemplate.setSharedVars(sharedVars);
        // 注入sqls tag
        BeetlRenderFactory.groupTemplate.registerTag("sqls", SqlsTag.class);

        // 正式环境,将System.out、err输出到log中
        if (!devMode) {
            System.setOut(new LogPrintStream(false));
            System.setErr(new LogPrintStream(true));
        }

        // 启动
        onEasyStart();
    }

    public abstract void constant(Constants me);
    public abstract void route(Routes me);
    public abstract void mapping(ActiveRecordPlugin arp);
    public abstract void plugin(Plugins plugins);
    public abstract void onEasyStart();
}
