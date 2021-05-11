package application;

import java.sql.Date;

public class Goods {
	public String supp_comp;
	public int product_id;
	public String product_name;
	public String ptype;
	public double wholesale_price;
	public double retail_price;
	public int goods_count;
	public Date exp_date;
	public String storage_location;
	
	public Goods(String supp_comp, int product_id, String product_name, String ptype, double wholesale_price, double retail_price, int goods_count, 
		String storage_location , Date exp_date) {
		this.supp_comp = supp_comp;
		this.product_id = product_id;
		this.product_name = product_name;
		this.ptype = ptype;
		this.wholesale_price = wholesale_price;
		this.retail_price = retail_price;
		this.goods_count = goods_count;
		this.exp_date = exp_date;
		this.storage_location = storage_location;
	}

	public String getStorage_location() {
		return storage_location;
	}

	public void setStorage_location(String storage_location) {
		this.storage_location = storage_location;
	}

	public String getSupp_comp() {
		return supp_comp;
	}

	public void setSupp_comp(String supp_comp) {
		this.supp_comp = supp_comp;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String type) {
		this.ptype = type;
	}

	public double getWholesale_price() {
		return wholesale_price;
	}

	public void setWholesale_price(double wholesale_price) {
		this.wholesale_price = wholesale_price;
	}

	public double getRetail_price() {
		return retail_price;
	}

	public void setRetail_price(double retail_price) {
		this.retail_price = retail_price;
	}

	public int getGoods_count() {
		return goods_count;
	}

	public void setGoods_count(int goods_count) {
		this.goods_count = goods_count;
	}

	public Date getExp_date() {
		return exp_date;
	}

	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}
}

