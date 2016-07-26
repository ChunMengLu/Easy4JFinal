package net.dreamlu.example.model;

import com.jfinal.plugin.activerecord.Page;
import net.dreamlu.example.model.base.BaseBlog;

/**
 * Generated by Easy4JFinal.
 */
@SuppressWarnings("serial")
public class Blog extends BaseBlog<Blog> {
	public static final Blog dao = new Blog();
	
	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Blog> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from t_blog order by id asc");
	}
}