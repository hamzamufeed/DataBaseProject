package application;

import java.sql.Date;

public class Orders {
	private int cid;
	private int order_num;
	private Date order_date;
	
	public Orders(int order_num, int cid, Date order_date) {
		this.cid = cid;
		this.order_num = order_num;
		this.order_date = order_date;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getOrder_num() {
		return order_num;
	}

	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}

	public Date getOrder_date() {
		return order_date;
	}

	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}
	
}
