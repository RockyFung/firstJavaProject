package rocky.domain;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	// 够无耻的中存储的多个cartItem
	private Map<String,CartItem>cartItems = new HashMap<String,CartItem>();
	// 购物车内商品价格总计
	private double total;
	
	public Map<String, CartItem> getCartItems() {
		return cartItems;
	}
	public void setCartItems(Map<String, CartItem> cartItems) {
		this.cartItems = cartItems;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
	
}
