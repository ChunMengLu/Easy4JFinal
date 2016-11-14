package net.dreamlu.easy.commons.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

/**
 * 使用owner解析application.properties文件
 * @author L.cm
 */
@Sources("classpath:application.properties")
public interface ApplicationConfig extends Config {
	
}
