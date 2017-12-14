package rocky.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocky.domain.PageBean;
import rocky.domain.Product;
import rocky.service.ProductService;

public class ProductListByCidServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String cid = request.getParameter("cid");
		// 初始化值
		String cp = request.getParameter("currentPage");
		if (cp==null) {
			cp="1";
		}
		// 获取当前页
		int currentPage = Integer.parseInt(cp);
		int currentCount = 12;
		ProductService service = new ProductService();
		PageBean<Product> pageBean = service.findProductByCid(cid,currentPage,currentCount);
		request.setAttribute("pageBean", pageBean);
		request.setAttribute("cid", cid);
		
		// 获得客户端携带的pids的cookie
		Cookie[] cookies = request.getCookies();
		// 记录历史商品
		List<Product> historyProduct = new ArrayList<Product>(); 
		if (cookies!=null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					String pids = cookie.getValue();
					String[] split = pids.split(",");
					for (String pid : split) {
						Product product = service.findProductByPid(pid);
						historyProduct.add(product);
					}
				}
			}
		}
		// 将历史记录的集合放到域
		request.setAttribute("historyProduct", historyProduct);
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}