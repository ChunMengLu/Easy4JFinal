package net.dreamlu.example.common;


import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.upload.OreillyCos;

import net.dreamlu.easy.commons.config.EasyConfig;
import net.dreamlu.easy.commons.upload.EasyFileRenamePolicy;
import net.dreamlu.example.blog.BlogController;
import net.dreamlu.example.index.IndexController;
import net.dreamlu.example.model._MappingKit;
import net.dreamlu.example.ws.TimeTaskTest;

/**
 * API引导式配置
 */
public class DemoConfig extends EasyConfig {

	@Override
	public void constant(Constants me) {
        me.setMainRenderFactory(new BeetlRenderFactory());
	}

	@Override
	public void route(Routes me) {
		me.add("/", IndexController.class, "/index");	// 第三个参数为该Controller的视图存放路径
		me.add("/blog", BlogController.class);			// 第三个参数省略时默认与第一个参数值相同，在此即为 "/blog"
	}

	@Override
	public void mapping(ActiveRecordPlugin arp) {
		_MappingKit.mapping(arp);
	}

	@Override
    public void configHandler(Handlers me) {
        super.configHandler(me);
    }

    @Override
	public void plugin(Plugins plugins) {
//	    plugins.add(new RedisPlugin("main", "127.0.0.1"));
	}

    @Override
	public void onEasyStart() {
        // 注意 以 / 开头表绝对路径 比如 /var/home/tomcat
        String fileRenameXxx = "image/{yyyy}{mm}{dd}/{time}{rand:6}";
        OreillyCos.setFileRenamePolicy(new EasyFileRenamePolicy(fileRenameXxx));
	    TimeTaskTest.start();
	}
}
