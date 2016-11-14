package net.dreamlu.easy.commons.config;

import java.util.Date;

import javax.servlet.ServletContext;

import org.aeonbits.owner.ConfigFactory;

import com.jfinal.core.JFinal;
import com.jfinal.upload.OreillyCos;

import net.dreamlu.easy.commons.logs.LogPrintStream;
import net.dreamlu.easy.commons.session.SessionRepositoryRequestWrapper;
import net.dreamlu.easy.commons.upload.EasyFileRenamePolicy;
import net.dreamlu.easy.commons.utils.WebUtils;

/**
 * Easy4JFinal
 * @author L.cm
 */
public final class Easy4JFinal {
	private static final Easy4JFinal me = new Easy4JFinal();
	
	private Easy4JFinal() {}
	
	public static Easy4JFinal me() {
		return me;
	}
	
	// 版本号
	public static final String VERSION = "1.0";
	
	private ApplicationConfig cfg;
	
	void initCfg() {
		cfg = ConfigFactory.create(ApplicationConfig.class);
	}
	
	void initAfterStart() {
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
		
		// 初始化Session
		SessionRepositoryRequestWrapper.initCfg(cfg);
	}
	
	public final ApplicationConfig getCfg() {
		return cfg;
	}
	
}
