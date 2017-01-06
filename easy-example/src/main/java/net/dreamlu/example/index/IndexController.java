package net.dreamlu.example.index;

import net.dreamlu.easy.commons.base.EasyController;

/**
 * IndexController
 */
public class IndexController extends EasyController {
	public void index() {
		setSessionAttr("hhh", "xxxx");
		System.out.println(getSessionAttr("hhh"));
		render("index.html");
	}
}