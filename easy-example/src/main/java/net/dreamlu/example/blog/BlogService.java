package net.dreamlu.example.blog;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import net.dreamlu.easy.commons.annotation.Inject;
import net.dreamlu.easy.commons.annotation.Service;
import net.dreamlu.example.index.IndexService;
import net.dreamlu.example.model.Blog;

@Service
public class BlogService {
	
	@Inject
	private IndexService indexService;

	@Before(BlogInterceptor.class)
	public Page<Blog> findByPage(int pageNumber, int pageSize) {
		indexService.saveTest();
		return Blog.dao.paginate(pageNumber, pageSize);
	}
}
