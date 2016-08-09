package net.dreamlu.easy.ui.jsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.LoopTag;
import javax.servlet.jsp.tagext.IterationTag;

import org.apache.taglibs.standard.tag.common.core.ForEachSupport;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Sqls;

/**
 * 设计一个和 Sqls.get(key); 一起用的页面标签，提高工作效率。
 * <pre>
 * &lt;db:sqls var=&quot;x&quot; key=&quot;findAll&quot;&gt;
 *     ${x}
 * &lt;/db:sqls&gt;
 * </pre>
 * @author L.cm
 *
 */
public class SqlsTag extends ForEachSupport implements LoopTag, IterationTag {

	private static final long serialVersionUID = 6139426859096232015L;

	private String key;
	private String paras;
	private String limit;
	private String cacheName;
	
	public SqlsTag() {
		super();
		init();
	}
	
	private void init() {
		key = null;
		paras = null;
		limit = null;
		cacheName = null;
	}
	
	@Override
	public int doStartTag() throws JspException {
		if (StrKit.isBlank(key)) {
			throw new JspException("sqls tag attribute key can not be blank!");
		}
		String sql = Sqls.get(key);
		final List<Object> parameterList = new ArrayList<Object>();
		
		String cacheKey = key;
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
		if (StrKit.isBlank(cacheName)) {
			rawItems = Db.find(cacheKey, sql, parameterList.toArray());
		} else {
			rawItems = Db.findByCache(cacheName, cacheKey, sql, parameterList.toArray());
		}
		return super.doStartTag();
	}

	@Override
	public void release() {
		super.release();
		init();
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setParas(String paras) {
		this.paras = paras;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
}
