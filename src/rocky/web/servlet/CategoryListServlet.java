package rocky.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import rocky.domain.Category;
import rocky.service.ProductService;
import rocky.utils.JedisPoolUtils;

public class CategoryListServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ProductService service = new ProductService();
		// 先从缓存中查询categoryList，没有再从数据库查询
		// 回去jedis对象
		Jedis jedis = JedisPoolUtils.getJedis();
		String categoryListStr = jedis.get("categoryListJson");
		if (categoryListStr == null) {
			// 从数据库查询
			List<Category>categoryList = service.findAllCategory();
			Gson gson = new Gson();
			String json = gson.toJson(categoryList);
			// 把数据存到redis
			jedis.set("categoryListJson",json);
		}
		
		
		
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(categoryListStr);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}