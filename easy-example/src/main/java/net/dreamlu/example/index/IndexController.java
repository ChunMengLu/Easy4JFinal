package net.dreamlu.example.index;

import net.dreamlu.easy.commons.annotation.Inject;
import net.dreamlu.easy.commons.base.EasyController;
import net.dreamlu.example.blog.BlogService;

/**
 * IndexController
 */
public class IndexController extends EasyController {
	@Inject
	private BlogService userService;
	
	public void index() {
		userService.hello();
		
	    setSessionAttr("hhh", "xxxx");
	    System.out.println(getSessionAttr("hhh"));
		render("index.html");
	}
}