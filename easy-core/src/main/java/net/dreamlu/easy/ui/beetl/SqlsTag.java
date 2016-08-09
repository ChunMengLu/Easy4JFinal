package net.dreamlu.easy.ui.beetl;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.Sqls;
import org.beetl.core.GeneralVarTagBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 设计一个和 Sqls.get(key); 一起用的页面标签，提高工作效率。
 * <pre>
 * &lt;#sqls key=&quot;xxx&quot; paras=&quot;1;2;3&quot; limit=&quot;5&quot; cacheName=&quot;blog&quot;; x&gt;
 *     ${x.id}, ${x.title} &lt;br/&gt;
 * &lt;/#sqls&gt;
 * </pre>
 * @author L.cm
 */
public class SqlsTag extends GeneralVarTagBinding {

	@Override
	@SuppressWarnings("unchecked")
	public void render() {
		Map<String, Object> attrs = (Map<String, Object>) this.args[1];
		String sqlKey    = (String) attrs.get("key");
		String paras     = (String) attrs.get("paras");
		String limit     = (String) attrs.get("limit");
		String cacheName = (String) attrs.get("cacheName");

		// sqlKey不能为空
		if (StrKit.isBlank(sqlKey)) {
			throw new RuntimeException("sqls tag attribute key can not be blank!");
		}
		// 获取配置文件中的sql语句
		String sql = Sqls.get(sqlKey);
		final List<Object> parameterList = new ArrayList<Object>();

		String cacheKey = sqlKey;
		if (StrKit.notBlank(paras)) {
			cacheKey += paras;
			Object[] paraArray = paras.split(";");
			parameterList.addAll(Arrays.asList(paraArray));
		}
		// limit处理，limit直接拼接就好，limit位于sql尾部。
		if (StrKit.notBlank(limit)) {
			cacheKey += limit;
			// sql 本身含有LIMIT时的处理
			String tempSql = sql.toUpperCase();
			int index = tempSql.indexOf("LIMIT");
			if (index != -1) {
				sql = sql.substring(0, index);
			}
			sql += " LIMIT " + limit;
		}
		// 对cache的处理
		List<Record> list = null;
		if (StrKit.isBlank(cacheName)) {
			list = Db.find(cacheKey, sql, parameterList.toArray());
		} else {
			list = Db.findByCache(cacheName, cacheKey, sql, parameterList.toArray());
		}
		// 数据渲染
		if (null != list) {
			for (Record record : list) {
				this.binds(record);
				this.doBodyRender();
			}
		}
	}

}