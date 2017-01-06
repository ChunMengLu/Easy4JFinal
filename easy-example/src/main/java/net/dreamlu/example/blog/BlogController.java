package net.dreamlu.example.blog;


import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

import net.dreamlu.easy.commons.annotation.Inject;
import net.dreamlu.easy.commons.base.EasyController;
import net.dreamlu.example.model.Blog;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class BlogController extends EasyController {
	@Inject
	private BlogService userService;
	
	public void index() {
		Page<Blog> blogPage = userService.findByPage(getParaToInt(0, 1), 10);
		setAttr("blogPage", blogPage);
		render("blog.html");
	}
	
	public void add() {
	}
	
	@Before(BlogValidator.class)
	public void save() {
		getModel(Blog.class).save();
		redirect("/blog");
	}
	
	public void edit() {
		setAttr("blog", Blog.dao.findById(getParaToInt()));
	}
	
	public void upload() {
		try {
			List<UploadFile> list = getFiles();
			System.out.println(JsonKit.toJson(list));
			renderJson(list);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			renderText(e.getMessage());
		}
	}
	
	@Before(BlogValidator.class)
	public void update() {
		getModel(Blog.class).update();
		redirect("/blog");
	}
	
	public void delete() {
		Blog.dao.deleteById(getParaToInt());
		redirect("/blog");
	}
}


