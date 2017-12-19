package rocky.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import rocky.dao.Order;
import rocky.dao.OrderItem;
import rocky.dao.ProductDao;
import rocky.domain.Category;
import rocky.domain.PageBean;
import rocky.domain.Product;
import rocky.utils.DataSourceUtils;

public class ProductService {

	public List<Product> findHotProduct() {
		ProductDao dao = new ProductDao();
		List<Product> productList = null;
		try {
			productList = dao.findHotProduct();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productList;
	}

	public List<Product> findNewProduct() {
		ProductDao dao = new ProductDao();
		List<Product> productList = null;
		try {
			productList = dao.findNewProduct();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productList;
	}

	public List<Category> findAllCategory() {
		ProductDao dao = new ProductDao();
		List<Category> categoryList = null;
		try {
			categoryList = dao.findAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categoryList;
	}

	public PageBean<Product> findProductByCid(String cid, int currentPage, int currentCount) {
		ProductDao dao = new ProductDao();
		PageBean<Product> pageBean = new PageBean<Product>();
		
		// 当前页
		pageBean.setCurrentPage(currentPage);
		// 每页显示条数
		pageBean.setCurrentCount(currentCount);
		// 总条数
		int totalCount = 0;
		try {
			totalCount = dao.getCount(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		// 总页数
		int totalPage = (int) Math.ceil(totalCount*1.0 / currentCount);
		pageBean.setTotalPage(totalPage);
		// 当前页显示的数据
		int index =  (currentPage - 1) * currentCount;
		List<Product>list = null;
		try {
			list = dao.findProductByPage(cid,index,currentCount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageBean.setList(list);
		
		return pageBean;
	}

	public Product findProductByPid(String pid) {
		ProductDao dao = new ProductDao();
		Product product = null;
		try {
			product = dao.findProductByPid(pid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return product;
	}

	// 提交订单
	public void submitOrder(Order order) {
		ProductDao dao = new ProductDao();
		
		try {
			// 开启事务
			DataSourceUtils.startTransaction();
			// 存储order
			dao.addOrders(order);
			// 存储orderItem
			dao.addOrderItem(order);
		} catch (SQLException e) {
			// 回滚
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public void updateOrderAdrr(Order order) {
		ProductDao dao = new ProductDao();
		try {
			dao.updateOrderAdrr(order);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public List<Order> findAllOrders(String uid) {
		ProductDao dao = new ProductDao();
		List<Order> orderList = null;
		try {
			orderList = dao.findAllOrders(uid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderList;
	}

	public List<Map<String, Object>> findAllOrderItemByOid(String oid) {
		ProductDao dao = new ProductDao();
		List<Map<String, Object>> mapList = null;
		try {
			mapList = dao.findAllOrderItemByOid(oid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapList;
	}

}
