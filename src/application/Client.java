package application;


public class Client {
	private int id;
	private String cname;
	private String area;
	private int delegate_id;
	private int phone_num;
	private double debt = 0;
	
	public Client(int id, String cname, String area, int delagate_id,int phone_num) {
		this.id = id;
		this.cname = cname;
		this.area = area;
		this.delegate_id = delagate_id;
		this.phone_num = phone_num;
		this.debt = 0;
	}

	public double getDebt() {
		return debt;
	}

	public void setDebt(double debt) {
		this.debt = debt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getDelegate_id() {
		return delegate_id;
	}

	public void setDelegate_id(int delegate_id) {
		this.delegate_id = delegate_id;
	}

	public int getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(int phone_num) {
		this.phone_num = phone_num;
	}
}

