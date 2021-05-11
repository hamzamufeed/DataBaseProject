package application;


public class CEO extends Employee{
	
	public CEO(int id,int manager_id, String name, String address, double salary, int working_hours, int phone_num) {
		super(id,manager_id,name,address,salary,working_hours,phone_num,null);
	}

}
