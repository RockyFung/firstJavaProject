package rocky.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import rocky.domain.Category;
import rocky.domain.Product;
import rocky.utils.DataSourceUtils;

public class ProductDao {

	public List<Product> findHotProduct() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where is_hot=? limit ?,?";
		List<Product> query = queryRunner.query(sql, new BeanListHandler<Product>(Product.class),1,0,9);
		return query;
	}

	public List<Product> findNewProduct() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product  order by pdate desc limit ?,?";
		List<Product> query = queryRunner.query(sql, new BeanListHandler<Product>(Product.class),0,9);
		return query;
	}

	public List<Category> findAllCategory() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from category";
		List<Category> query = queryRunner.query(sql, new BeanListHandler<Category>(Category.class));
		return query;
	}

	public int getCount(String cid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from product where cid=?";
		Long query = (Long) queryRunner.query(sql, new ScalarHandler(),cid);
		return query.intValue();
	}

	public List<Product> findProductByPage(String cid, int index, int currentCount) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where cid=? limit ?,?";
		List<Product> query = queryRunner.query(sql, new BeanListHandler<Product>(Product.class),cid,index,currentCount);
		return query;
	}

	public Product findProductByPid(String pid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where pid=?";
		Product product = queryRunner.query(sql, new BeanHandler<Product>(Product.class),pid);
		return product;
	}

	// 向orders插入数据
	public void addOrders(Order order) throws SQLException {
		QueryRunner queryRunner = new QueryRunner();
		String sql = "insert into orders value(?,?,?,?,?,?,?,?)";
		Connection conn = DataSourceUtils.getConnection();
		queryRunner.update(conn,sql,order.getOid(),order.getOrdertime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid());
		
	}

	// 向orderItem插入数据
	public void addOrderItem(Order order) throws SQLException {
		QueryRunner queryRunner = new QueryRunner();
		String sql = "insert into orderitem value(?,?,?,?,?)";
		Connection conn = DataSourceUtils.getConnection();
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			queryRunner.update(conn,sql,orderItem.getItemid(),orderItem.getCount(),orderItem.getSubtotal(),orderItem.getProduct().getPid(),orderItem.getOrder().getOid());
		}
	}

	public void updateOrderAdrr(Order order) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update orders set address=?,name=?,telephone=? where oid=? ";
		queryRunner.update(sql,order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
		
	}

	public List<Order> findAllOrders(String uid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orders where uid=?";
		List<Order>orderList = queryRunner.query(sql, new BeanListHandler<Order>(Order.class),uid);
		return orderList;
	}

	public List<Map<String, Object>> findAllOrderItemByOid(String oid) throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from orderitem i,product p where i.pid=p.pid and i.oid=?";
		List<Map<String, Object>>mapList = queryRunner.query(sql, new MapListHandler(),oid);
		return mapList;
	}

}
