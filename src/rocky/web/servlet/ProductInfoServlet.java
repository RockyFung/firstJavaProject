package rocky.web.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocky.domain.Product;
import rocky.service.ProductService;

public class ProductInfoServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 返回列表页面需要的数据
		String currentPage = request.getParameter("currentPage");
		String pid = request.getParameter("pid");
		
		ProductService service = new ProductService();
		Product product = service.findProductByPid(pid);
		
		// 历史记录
		List<Product>historyList=null;
		
		request.setAttribute("product", product);
		request.setAttribute("currentPage", currentPage);
		
		// 获得客户端携带的cookie
		Cookie[] cookies = request.getCookies();
		String pids= pid;
		if (cookies!=null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					pids = cookie.getValue();//1,2,3(每次pid用“，”隔开)
					String[] split = pids.split(",");
					List<String> asList = Arrays.asList(split);
					LinkedList<String> list = new LinkedList<String>(asList);
					// 判断这个集合中是否有当前的pid
					if (list.contains(pid)) {
						list.remove(pid);
					}
					// 如果没有就直接放到最上面
					list.addFirst(pid);
					// 在将list转成string
					StringBuffer sb = new StringBuffer();
					for (int i=0;i<list.size()&&i<7;i++) {
						sb.append(list.get(i));
						sb.append(",");
					}
					
					// 去掉最后一个“，”
					pids = sb.substring(0,sb.length()-1);
				}
			}
		}
		
		// 转发前创建cookie，存储pid
		Cookie newCookie = new Cookie("pids", pids);
		response.addCookie(newCookie);
		request.getRequestDispatcher("/product_info.jsp").forward(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}