package rocky.dao;

import rocky.domain.Product;

public class OrderItem {
	private String itemid; // 订单项id
	private int count; // 每个商品的数量
	private double subtotal; // 每个商品价格小计
	private Product product;// 商品
	private Order order;// 订单项属于哪个订单
	
	
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	
	
}
