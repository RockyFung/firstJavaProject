package rocky.service;


import java.util.List;
import java.util.Map;

import rocky.dao.Order;
import rocky.domain.Category;
import rocky.domain.Product;

public interface AdminService {

	public List<Category> findAllCategory();

	public void saveProduct(Product product);

	public List<Order> findAllOrders();

	public List<Map<String, Object>> findOrderInfoByOid(String oid) ;

}
