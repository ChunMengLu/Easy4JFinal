package net.dreamlu.easy.core;

import javax.sql.DataSource;

import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;

import net.dreamlu.easy.commons.generator.EasyGenerator;

/**
 * GeneratorDemo
 */
public class GeneratorDemo {
	
	public static DataSource getDataSource() {
		String jdbcUrl  = "jdbc:mysql://127.0.0.1/example?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
		String user     = "root";
		String password = "root";

		DruidPlugin plugin = new DruidPlugin(jdbcUrl, user, password);
		plugin.start();
		return plugin.getDataSource();
	}
	
	public static void main(String[] args) {
		// base model 所使用的包名
		String baseModelPackageName = "bios.platform.model.base";
		// base model 文件保存路径
//		String baseModelOutputDir = PathKit.getWebRootPath() + "/../src/bios/platform/model/base";
		
		String baseModelOutputDir = "C:\\code\\x/src/bios/platform/model/base";
		
		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "bios.platform.model";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";

		// 创建生成器
		EasyGenerator gernerator = new EasyGenerator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		// 设置数据库方言
		gernerator.setDialect(new MysqlDialect());
		// 添加不需要生成的表名
//		gernerator.addExcludedTable("adv");
		// 设置是否在 Model 中生成 dao 对象
		gernerator.setGenerateDaoInModel(true);
		// 设置是否生成字典文件
		gernerator.setGenerateDataDictionary(true);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
		gernerator.setRemovedTableNamePrefixes("t_");
		
		// 配置实用模版生成
		gernerator.setGenerateTemplate(true);
		gernerator.setTemplateDir("C:\\code\\xx");
		gernerator.setOutPutDir("C:\\code\\xxx");
		// 生成
		gernerator.generate();
	}
}




