package application;

public class SupplyingCompany {
	private String name;
	private double financial_value=0;
	
	public SupplyingCompany(String name, double financial_value) {
		this.name = name;
		this.financial_value = financial_value;
	}
	
	public SupplyingCompany(String name) {
		this.name = name;
	}

	public double getFinancial_value() {
		return financial_value;
	}

	public void setFinancial_value(double financial_value) {
		this.financial_value = financial_value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

