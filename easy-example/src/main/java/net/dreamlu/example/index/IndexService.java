package net.dreamlu.example.index;

import com.jfinal.aop.Before;

import net.dreamlu.easy.commons.annotation.Component;
import net.dreamlu.example.blog.BlogInterceptor;

@Component
public class IndexService {

	@Before(BlogInterceptor.class)
	public void saveTest() {
		System.out.println(this.getClass() + "\t saveTest");
	}

}
