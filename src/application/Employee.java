package application;

public class Employee {
	private int id;
	private int manager_id;
	private String ename;
	private String address;
	private double salary;
	private int working_hours;
	private int phone_num;
	private String Dist_area;
	
	public Employee(int id, int manager_id, String ename, String address, double salary, int working_hours, int phone_num, String Dist_area) {
		this.id = id;
		this.manager_id = manager_id;
		this.ename = ename;
		this.address = address;
		this.salary = salary;
		this.working_hours = working_hours;
		this.phone_num = phone_num;
		this.Dist_area = Dist_area;
	}

	public String getDist_area() {
		return Dist_area;
	}

	public void setDist_area(String dist_area) {
		Dist_area = dist_area;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String name) {
		this.ename = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public int getWorking_hours() {
		return working_hours;
	}

	public void setWorking_hours(int working_hours) {
		this.working_hours = working_hours;
	}

	public int getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(int phone_num) {
		this.phone_num = phone_num;
	}

	public int getManager_id() {
		return manager_id;
	}

	public void setManager_id(int manager_id) {
		this.manager_id = manager_id;
	}	
}

