package net.dreamlu.example.index;

import net.dreamlu.easy.commons.annotation.Controller;
import net.dreamlu.easy.commons.base.EasyController;

/**
 * IndexController
 */
//viewPath为该Controller的视图存放路径
@Controller( value="/", viewPath = "/index")
public class IndexController extends EasyController {
	public void index() {
		setSessionAttr("hhh", "xxxx");
		System.out.println(getSessionAttr("hhh"));
		render("index.html");
	}
}