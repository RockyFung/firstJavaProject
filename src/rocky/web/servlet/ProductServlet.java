package rocky.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.NEW;

import redis.clients.jedis.Jedis;
import rocky.dao.Order;
import rocky.dao.OrderItem;
import rocky.dao.User;
import rocky.domain.Cart;
import rocky.domain.CartItem;
import rocky.domain.Category;
import rocky.domain.PageBean;
import rocky.domain.Product;
import rocky.service.ProductService;
import rocky.utils.CommonsUtils;
import rocky.utils.JedisPoolUtils;

@SuppressWarnings("serial")
public class ProductServlet extends BaseServlet {
/*
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获得请求的调用方法名
		String methodName = request.getParameter("method");
		if ("productListByCid".equals(methodName)) {
			productListByCid(request, response);
		}else if ("categoryList".equals(methodName)) {
			categoryList(request, response);
		}else if ("index".equals(methodName)) {
			index(request, response);
		}else if ("productInfo".equals(methodName)) {
			productInfo(request, response);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
*/
	
	
	// 通过BaseServlet的反射调用下面的方法
	// 模块中的功能是通过方法区分的
	
	// 获得我的订单
	public void myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
		HttpSession session = request.getSession();
		// 判断登陆
		User user = (User) session.getAttribute("user");
		if (user==null) {
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return;
		}
		
		// 获取该用户的所有订单集合
		ProductService service = new ProductService();
		List<Order>orderList = service.findAllOrders(user.getUid());
		// 循环所有订单，为每个订单填充订单项集合信息
		if (orderList != null) {
			for (Order order : orderList) {
				// 获得每个订单的oid
				String oid = order.getOid();
				List<Map<String, Object>>mapList = service.findAllOrderItemByOid(oid);
				for (Map<String, Object> map : mapList) {
					try {
						// 封装orderItem
						OrderItem orderItem = new OrderItem();
						BeanUtils.populate(orderItem, map);
						// 封装product
						Product product = new Product();
						BeanUtils.populate(product, map);
						// 把orderItem和product装入order中
						orderItem.setProduct(product);
						order.getOrderItems().add(orderItem);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("/order_list.jsp").forward(request, response);
		
		
	}
	
	
	// 确认订单
	public void confirmOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
		// 更新收货人信息
		Map<String, String[]> parameterMap = request.getParameterMap();
		Order order = new Order();
		try {
			BeanUtils.populate(order, parameterMap);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(parameterMap);
		ProductService service = new ProductService();
		service.updateOrderAdrr(order);
		
		// 在线支付
		// 接入第三方接口--易宝支付
		
		
	}
	
	// 提交订单
	public void submitOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
		HttpSession session = request.getSession();
		// 判断登陆
		User user = (User) session.getAttribute("user");
		if (user==null) {
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return;
		}
		// 封装order对象
		Order order = new Order();
		
		String oid = CommonsUtils.getUUID();
		order.setOid(oid);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateStr = format.format(new Date());
		Date date = format.parse(dateStr);
		order.setOrdertime(date);
		
		Cart cart = (Cart) session.getAttribute("cart");
		double total = cart.getTotal();
		order.setTotal(total);
		order.setState(0);
		order.setAddress(null);
		order.setName(null);
		order.setTelephone(null);
		order.setUser(user);
		
		// 订单项
		Map<String, CartItem> cartItems = cart.getCartItems();
		for (Map.Entry<String, CartItem>entry:cartItems.entrySet()) {
			// 取出每个购物项
			CartItem cartItem = entry.getValue();
			// 创建新订单项
			OrderItem item = new OrderItem();
			item.setItemid(CommonsUtils.getUUID());
			item.setCount(cartItem.getBuyNum());
			item.setSubtotal(cartItem.getSubtotal());
			item.setProduct(cartItem.getProduct());
			item.setOrder(order);
			// 将该订单添加到订单项中
			order.getOrderItems().add(item);
		}
		
		// 传递数据
		ProductService service = new ProductService();
		service.submitOrder(order);
		
		// 将order存到session
		session.setAttribute("order", order);
		response.sendRedirect(request.getContextPath() + "/order_info.jsp");
		// 页面跳转
	}
	
	// 清空购物车 clearCart
	public void clearCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 删除名称为cart的session
		HttpSession session = request.getSession();
		session.removeAttribute("cart");
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	// 删除商品deleteProductByPid
	public void deleteProductByPid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String pid = request.getParameter("pid");
		// 删除session中的购物车中的商品
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart!=null) {
			 Map<String, CartItem>cartItem = cart.getCartItems();
			// 修改总价
			cart.setTotal(cart.getTotal() - cartItem.get(pid).getSubtotal());
			// 删除商品
			cartItem.remove(pid);
		}
		session.setAttribute("cart", cart);
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	// 商品添加到购物车
	public void addProductToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		HttpSession session = request.getSession();
		// 获得商品
		String pid = request.getParameter("pid");
		Product product = service.findProductByPid(pid);
		// 购买数量
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		// 计算小计
		double subtotal = product.getShop_price() * buyNum;
		// 封装
		CartItem item = new CartItem();
		item.setProduct(product);
		item.setBuyNum(buyNum);
		item.setSubtotal(subtotal);
		// 放入购物车
		// 判断session是否有购物车
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart==null) {
			cart = new Cart();
		}
		// 判断购物车中是否已有
		Map<String, CartItem> cartItems = cart.getCartItems();
		double newSubtotal = 0.0;
		if (cartItems.containsKey(pid)) {
			CartItem cartItem = cartItems.get(pid);
			// 修改数量
			int oldBuyNum = cartItem.getBuyNum();
			oldBuyNum+=buyNum;
			cartItem.setBuyNum(oldBuyNum);
			// 修改小计
			double oldSubtotal = cartItem.getSubtotal();
			newSubtotal = buyNum * product.getShop_price();
			cartItem.setSubtotal(newSubtotal+oldSubtotal);
			
			cart.setCartItems(cartItems);
		}else {
			cart.getCartItems().put(pid, item);
			newSubtotal = buyNum * product.getShop_price();
		}
		
		// 计算总计
		double total = cart.getTotal() + newSubtotal;
		cart.setTotal(total);
		// 发送
		session.setAttribute("cart", cart);
		// 必须用重定向，更改地址
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
		
		
	}
	
	
	// 显示商品分类的功能
	public void categoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	
	// 显示首页功能
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ProductService service = new ProductService();
		
		// 热门商品
		List<Product>hotProductList = service.findHotProduct();
		
		// 最新商品
		List<Product>newProductList = service.findNewProduct();
		
		// 分类数据
		//List<Category>categoryList = service.findAllCategory();
		//request.setAttribute("categoryList", categoryList);
		request.setAttribute("hotProductList", hotProductList);
		request.setAttribute("newProductList", newProductList);
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
	
	// 商品详情
	
	public void productInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 返回列表页面需要的数据
		String currentPage = request.getParameter("currentPage");
		String pid = request.getParameter("pid");
		
		ProductService service = new ProductService();
		Product product = service.findProductByPid(pid);
		
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
	
	// 根据商品cid获取商品列表
	public void productListByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
	
}