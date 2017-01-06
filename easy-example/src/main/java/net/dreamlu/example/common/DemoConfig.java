package net.dreamlu.example.common;


import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Plugins;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

import net.dreamlu.easy.commons.config.EasyConfig;
import net.dreamlu.example.model._MappingKit;
import net.dreamlu.example.ws.TimeTaskTest;

/**
 * API引导式配置
 */
public class DemoConfig extends EasyConfig {

	@Override
	public void constant(Constants me) {

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
	    TimeTaskTest.start();
	}
}
