package rocky.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import rocky.dao.Order;
import rocky.domain.Category;
import rocky.service.AdminService;
import rocky.utils.BeanFactory;

@SuppressWarnings("serial")
public class AdmainServlet extends BaseServlet {

	//
	public void findOrderInfoByOid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//AdminService service = new AdminService();
		// 用解耦方式编码
		// 工厂+反射+配置文件
		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		
		String oid = request.getParameter("oid");
		List<Map<String, Object>>mapList = service.findOrderInfoByOid(oid);
		Gson gson = new Gson();
		String json = gson.toJson(mapList);
		System.out.println(json);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(json);
	}
	// 获得所有订单
	public void findAllOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		List<Order>orderList = service.findAllOrders();
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("/admin/order/list.jsp").forward(request, response);
	}
	
	// 找到所有分类
	public void findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminService service = (AdminService) BeanFactory.getBean("adminService");
		List<Category>categoryList = service.findAllCategory();
		Gson gson = new Gson();
		String json = gson.toJson(categoryList);
		response.setContentType("text/html;charset=UTF8");
		response.getWriter().write(json);
	}


}