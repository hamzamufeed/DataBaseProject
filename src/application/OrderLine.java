package application;

import java.sql.Date;

public class OrderLine extends Orders{
	private int product_id;
	private String product_name;
	private int product_count;
	private double price;
	
	public OrderLine(int order_num, int product_id, int cid ,String product_name, int product_count, double price, Date order_date){
		super(order_num, cid, order_date);
		this.product_count = product_count;
		this.product_id = product_id;
		this.product_name = product_name;
		this.price = price;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getProduct_count() {
		return product_count;
	}

	public void setProduct_count(int product_count) {
		this.product_count = product_count;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}

