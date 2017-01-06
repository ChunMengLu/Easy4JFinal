package net.dreamlu.easy.commons.generator;

import java.util.List;

import com.jfinal.plugin.activerecord.generator.TableMeta;

/**
 * 重写，不生成MappingKit
 * @author L.cm
 *
 */
public class MappingKitGenerator extends com.jfinal.plugin.activerecord.generator.MappingKitGenerator {

	public MappingKitGenerator(String mappingKitPackageName, String mappingKitOutputDir) {
		super(mappingKitPackageName, mappingKitOutputDir);
	}

	@Override
	public void generate(List<TableMeta> tableMetas) {
		
	}

	
}
