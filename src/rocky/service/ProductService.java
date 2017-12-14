package rocky.service;

import java.sql.SQLException;
import java.util.List;

import rocky.dao.ProductDao;
import rocky.domain.Category;
import rocky.domain.PageBean;
import rocky.domain.Product;

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

}
