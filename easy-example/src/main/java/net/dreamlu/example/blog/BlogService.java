package net.dreamlu.example.blog;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import net.dreamlu.easy.commons.annotation.Service;
import net.dreamlu.example.model.Blog;

@Service
public class BlogService {

	@Before(BlogInterceptor.class)
	public Page<Blog> findByPage(int pageNumber, int pageSize) {
		return Blog.dao.paginate(pageNumber, pageSize);
	}
}
