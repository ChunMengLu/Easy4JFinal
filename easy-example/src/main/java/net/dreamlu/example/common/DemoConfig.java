package net.dreamlu.example.common;


import com.jfinal.config.Constants;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import net.dreamlu.easy.config.EasyConfig;
import net.dreamlu.example.blog.BlogController;
import net.dreamlu.example.index.IndexController;
import net.dreamlu.example.model._MappingKit;

/**
 * API引导式配置
 */
public class DemoConfig extends EasyConfig {

	@Override
	public void constant(Constants me) {
		
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
	public void plugin(Plugins plugins) {
		
	}

	@Override
	public void onEasyStart() {
		
	}
}
