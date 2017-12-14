package rocky.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.sun.org.apache.bcel.internal.generic.NEW;

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

}
