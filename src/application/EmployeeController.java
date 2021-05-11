package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class EmployeeController implements Initializable {
	private String dbURL;
	private static String dbUsername = "root";
	private static String dbPassword = "987412365";
	private static String URL = "127.0.0.1";
	private static String port = "3306";
	private static String dbName = "Company";
	private static Connection con;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button company;

	@FXML
	private Button employee;

	@FXML
	private Button client;

	@FXML
	private Button order;

	@FXML
	private Button goods;

	@FXML
	private Button report;

	@FXML
	private Button logOut;

	@FXML
	private TableView<Employee> employee_table;

	@FXML
	private TableColumn<Employee, Integer> id_col;

	@FXML
	private TableColumn<Employee, String> name_col;

	@FXML
	private TableColumn<Employee, Integer> mgr_col;

	@FXML
	private TableColumn<Employee, String> address_col;

	@FXML
	private TableColumn<Employee, Double> salary_col;

	@FXML
	private TableColumn<Employee, Integer> hours_col;

	@FXML
	private TableColumn<Employee, Integer> phone_col;

	@FXML
	private TableColumn<Employee, String> area_col;

	@FXML
	private TextField id;

	@FXML
	private TextField name;

	@FXML
	private TextField mgr_id;

	@FXML
	private TextField address;

	@FXML
	private TextField salary;

	@FXML
	private TextField phone;

	@FXML
	private TextField hours;

	@FXML
	private TextField area;

	@FXML
	private Button addEmployee;

	@FXML
	private Button deleteEmployee;

	private ArrayList<Employee> employee_data;
	private ObservableList<Employee> employee_list;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		employee_data = new ArrayList<>();

		try {
			getdata();

		}catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		id_col.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("id"));

		name_col.setCellValueFactory(new PropertyValueFactory<Employee, String>("ename"));
		name_col.setCellFactory(TextFieldTableCell.<Employee>forTableColumn());
		name_col.setOnEditCommit(  
				(CellEditEvent<Employee, String> t) -> {
					((Employee) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setEname(t.getNewValue());
					updateName( t.getRowValue().getId(),t.getNewValue(),t.getRowValue().getDist_area());
				});

		mgr_col.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("manager_id"));
		mgr_col.setCellFactory(TextFieldTableCell.<Employee,Integer>forTableColumn(new IntegerStringConverter()));
		mgr_col.setOnEditCommit(        
				(CellEditEvent<Employee, Integer> t) -> {
					((Employee) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setManager_id(t.getNewValue());
					updateMgrID( t.getRowValue().getId(),t.getNewValue(),t.getRowValue().getDist_area());
				});

		address_col.setCellValueFactory(new PropertyValueFactory<Employee, String>("Address"));
		address_col.setCellFactory(TextFieldTableCell.<Employee>forTableColumn());
		address_col.setOnEditCommit(  
				(CellEditEvent<Employee, String> t) -> {
					((Employee) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setAddress(t.getNewValue());
					updateAddress( t.getRowValue().getId(),t.getNewValue(),t.getRowValue().getDist_area());
				});

		salary_col.setCellValueFactory(new PropertyValueFactory<Employee, Double>("Salary")); 
		salary_col.setCellFactory(TextFieldTableCell.<Employee,Double>forTableColumn(new DoubleStringConverter()));
		salary_col.setOnEditCommit(        
				(CellEditEvent<Employee, Double> t) -> {
					((Employee) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setSalary(t.getNewValue());
					updateSalary( t.getRowValue().getId(),t.getNewValue(),t.getRowValue().getDist_area());
				});

		hours_col.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("working_hours")); 
		hours_col.setCellFactory(TextFieldTableCell.<Employee,Integer>forTableColumn(new IntegerStringConverter()));
		hours_col.setOnEditCommit(        
				(CellEditEvent<Employee, Integer> t) -> {
					((Employee) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setWorking_hours(t.getNewValue());
					updateHours( t.getRowValue().getId(),t.getNewValue(),t.getRowValue().getDist_area());
				});

		phone_col.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("phone_num")); 
		phone_col.setCellFactory(TextFieldTableCell.<Employee,Integer>forTableColumn(new IntegerStringConverter()));
		phone_col.setOnEditCommit(        
				(CellEditEvent<Employee, Integer> t) -> {
					((Employee) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setPhone_num(t.getNewValue());
					updatePhone( t.getRowValue().getId(),t.getNewValue(),t.getRowValue().getDist_area());
				});

		area_col.setCellValueFactory(new PropertyValueFactory<Employee, String>("Dist_area"));
		area_col.setCellFactory(TextFieldTableCell.<Employee>forTableColumn());
		area_col.setOnEditCommit(  
				(CellEditEvent<Employee, String> t) -> {
					((Employee) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setDist_area(t.getNewValue());
					updateArea( t.getRowValue().getId(),t.getNewValue());
				});
		employee_table.setItems(get(employee_data));

	}

	public  ObservableList<Employee> get(ArrayList<Employee> c){
		employee_list = FXCollections.observableArrayList();

		for (int i = 0; i < c.size(); i++) {

			if(c.get(i) != null)
				employee_list.add(c.get(i));
		}
		return employee_list;
	}

	private void insertData(Employee emp) {
		try {
			connectDB();
			if(emp.getDist_area() == null) {
				System.out.println("Insert into employee values ("+emp.getId()+","+emp.getManager_id()+",'"+emp.getEname()+"'"
						+ ",'"+ emp.getAddress() +"',"+emp.getSalary()+","+ emp.getWorking_hours()+","+emp.getPhone_num()+");");

				ExecuteStatement("Insert into employee values ("+emp.getId()+","+emp.getManager_id()+",'"+emp.getEname()+"'"
						+ ",'"+ emp.getAddress() +"',"+emp.getSalary()+","+ emp.getWorking_hours()+","+emp.getPhone_num()+");");
			}
			else {
				System.out.println("Insert into delegate values ("+emp.getId()+","+emp.getManager_id()+",'"+emp.getEname()+"'"
						+ ",'"+ emp.getAddress() +"',"+emp.getSalary()+","+ emp.getWorking_hours()+","+emp.getPhone_num()+",'"+emp.getDist_area()+"');");    

				ExecuteStatement("Insert into delegate values ("+emp.getId()+","+emp.getManager_id()+",'"+emp.getEname()+"'"
						+ ",'"+ emp.getAddress() +"',"+emp.getSalary()+","+ emp.getWorking_hours()+","+emp.getPhone_num()+",'"+emp.getDist_area()+"');");
			}
			con.close();
			System.out.println("Connection closed" + employee_data.size());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	void getdata() throws SQLException, ClassNotFoundException {
		String SQL = "select * from employee";
		connectDB();
		System.out.println("Connection established");
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		while ( rs.next() ) {
			employee_data.add(new Employee(
					Integer.parseInt(rs.getString(1)),
					Integer.parseInt(rs.getString(2)),
					rs.getString(3),
					rs.getString(4),
					Double.parseDouble(rs.getString(5)),
					Integer.parseInt(rs.getString(6)),
					Integer.parseInt(rs.getString(7)),
					null));
		}
		SQL = "select * from delegate";
		stmt = con.createStatement();
		rs = stmt.executeQuery(SQL);

		while ( rs.next() ) {
			employee_data.add(new Employee(
					Integer.parseInt(rs.getString(1)),
					Integer.parseInt(rs.getString(2)),
					rs.getString(3),
					rs.getString(4),
					Double.parseDouble(rs.getString(5)),
					Integer.parseInt(rs.getString(6)),
					Integer.parseInt(rs.getString(7)),
					rs.getString(8)));
		}
		rs.close();
		con.close();
		System.out.println("Connection closed " + employee_data.size());
	}

	private void connectDB() throws ClassNotFoundException, SQLException {
		dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
		Properties p = new Properties();
		p.setProperty("user", dbUsername);
		p.setProperty("password", dbPassword);
		p.setProperty("useSSL", "false");
		p.setProperty("autoReconnect", "true");
		Class.forName("com.mysql.jdbc.Driver");

		con = DriverManager.getConnection (dbURL, p);
	}

	@FXML
	void clientOnAction(ActionEvent event) throws IOException {
		AnchorPane client = (AnchorPane)FXMLLoader.load(getClass().getResource("Client.fxml"));
		Scene scene = new Scene(client);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	@FXML
	void companyOnAction(ActionEvent event) throws IOException {
		AnchorPane company = (AnchorPane)FXMLLoader.load(getClass().getResource("Company.fxml"));
		Scene scene = new Scene(company);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	@FXML
	void employeeOnAction(ActionEvent event) throws IOException, ClassNotFoundException {
		AnchorPane employee = (AnchorPane)FXMLLoader.load(getClass().getResource("Employee.fxml"));
		Scene scene = new Scene(employee);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	@FXML
	void orderOnAction(ActionEvent event) throws IOException {
		AnchorPane order = (AnchorPane)FXMLLoader.load(getClass().getResource("Order.fxml"));
		Scene scene = new Scene(order);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	@FXML
	void goodsOnAction(ActionEvent event) throws IOException {
		AnchorPane goods = (AnchorPane)FXMLLoader.load(getClass().getResource("Goods.fxml"));
		Scene scene = new Scene(goods);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	@FXML
	void reportOnAction(ActionEvent event) throws IOException {
		AnchorPane report = (AnchorPane)FXMLLoader.load(getClass().getResource("Report.fxml"));
		Scene scene = new Scene(report);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	@FXML
	void logOut(ActionEvent event) throws IOException {
		AnchorPane logout = (AnchorPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
		Scene scene = new Scene(logout);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}

	public void ExecuteStatement(String SQL) throws SQLException {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL);
			stmt.close();	 
		}
		catch(SQLException s) {
			s.printStackTrace();
			System.out.println("SQL statement is not executed!");		  
		}
	}

	private void deleteRow(Employee row) {
		try {
			connectDB();
			if(row.getDist_area() == null) {
				String SQL = "select * from employee where manager_id="+row.getId()+";";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(SQL);
				if(rs.next() == true && row.getManager_id() == 0) 
					Alert("Couldn't delete! Remove manager permissions first");
				else {
					System.out.println("delete from employee where id="+row.getId() + ";");
					ExecuteStatement("delete from employee where id="+row.getId()+ ";");
					employee_table.getItems().remove(row);
				}
			}
			else {
				String SQL = "select * from delegate where manager_id="+row.getId()+";";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(SQL);
				String SQL2 = "select * from clients where delegate_id="+row.getId()+";";
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery(SQL2);
				if(rs.next() == false && rs2.next() == false) {
					System.out.println("delete from delegate where id="+row.getId() + ";");
					ExecuteStatement("delete from delegate where id="+row.getId()+ ";");
					employee_table.getItems().remove(row);
				}
				else if(row.getManager_id() == 0) {
					if(rs.next() == false)
						Alert("Couldn't delete! Remove manager permissions first");					
				}
				else if(rs2.next() == false)
					Alert("Couldn't delete! Remove delegate permissions first");
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void addEmployee(ActionEvent event) throws NumberFormatException, ClassNotFoundException, SQLException {
		if(!id.getText().matches("[0-9]+") || !mgr_id.getText().matches("[0-9]+") || !phone.getText().matches("[0-9]+") ||
				!hours.getText().matches("[0-9]+") || !salary.getText().matches("[0-9]+") || !(name.getText().length() > 0)) {
			Alert("Check your input fields");
		}
		else if(idExist(Integer.valueOf(id.getText()))){
			Alert("Employee ID Already Exists!");
		}
		else if(!idExist(Integer.valueOf(mgr_id.getText())) && Integer.valueOf(mgr_id.getText()) != 0) {
			Alert("Manager ID Doesn't Exist!");
		}
		else {
			if(area.getText().equals("")) {
				Employee emp = new Employee(
						Integer.valueOf(id.getText()),
						Integer.valueOf(mgr_id.getText()),
						name.getText(),
						address.getText(),
						Double.valueOf(salary.getText()),
						Integer.valueOf(hours.getText()),
						Integer.valueOf(phone.getText()),
						null);
				employee_list.add(emp);
				insertData(emp);
			}
			else {
				Employee emp = new Employee(
						Integer.valueOf(id.getText()),
						Integer.valueOf(mgr_id.getText()),
						name.getText(),
						address.getText(),
						Double.valueOf(salary.getText()),
						Integer.valueOf(hours.getText()),
						Integer.valueOf(phone.getText()),
						area.getText());
				employee_list.add(emp);
				insertData(emp);
			}
			id.clear();
			mgr_id.clear();
			name.clear();
			address.clear();
			salary.clear();
			hours.clear();
			phone.clear();
			area.clear();
		}
	}

	private boolean idExist(int id) throws ClassNotFoundException, SQLException {
		String SQL = "select * from employee where id="+id+";";
		connectDB();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		if(rs.last()) 
			return true;
		SQL = "select * from delegate where id="+id+";";
		rs = stmt.executeQuery(SQL);
		if(rs.last())
			return true;
		return false;
	}

	@FXML
	void deleteEmployee(ActionEvent event) {
		ObservableList<Employee> selectedRows = employee_table.getSelectionModel().getSelectedItems();
		ArrayList<Employee> rows = new ArrayList<>(selectedRows);
		rows.forEach(row -> { 
			deleteRow(row);
			employee_table.refresh();
		});   
	}

	public void updateName(int id, String name, String Dist_area) {	
		try {
			if(Dist_area == null) {
				System.out.println("update employee set ename = '"+name + "' where id = "+id);
				connectDB();
				ExecuteStatement("update employee set ename='"+name+"' where id="+id+";");
			}
			else {
				System.out.println("update delegate set ename = '"+name + "' where id = "+id);
				connectDB();
				ExecuteStatement("update delegate set ename='"+name+"' where id="+id+";");
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateMgrID(int id, int mgr_id, String Dist_area) {
		try {
			if(!idExist(mgr_id) && mgr_id != 0) {
				Alert("Manager ID Doesn't Exist!");
			}
			else if(Dist_area == null){
				System.out.println("update employee set manager_id = "+mgr_id + " where id = "+id);
				connectDB();
				ExecuteStatement("update employee set manager_id = "+mgr_id + " where id = "+id+";");
			}
			else {
				System.out.println("update delegate set manager_id = "+mgr_id + " where id = "+id);
				connectDB();
				ExecuteStatement("update delegate set manager_id = "+mgr_id + " where id = "+id+";");
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateAddress(int id, String address, String Dist_area) {	
		try {
			if(Dist_area == null) {
				System.out.println("update employee set address = '"+address + "' where id = "+id);
				connectDB();
				ExecuteStatement("update employee set address='"+address+"' where id="+id+";");
			}
			else {
				System.out.println("update delegate set address = '"+address + "' where id = "+id);
				connectDB();
				ExecuteStatement("update delegate set address='"+address+"' where id="+id+";");
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateSalary(int id, double salary, String Dist_area) {
		try {
			if(Dist_area == null) {
				System.out.println("update employee set salary = "+salary + " where id = "+id);
				connectDB();
				ExecuteStatement("update employee set salary = "+salary + " where id = "+id+";");
			}
			else {
				System.out.println("update delegate set salary = "+salary + " where id = "+id);
				connectDB();
				ExecuteStatement("update delegate set salary = "+salary + " where id = "+id+";");
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateHours(int id, int hours, String Dist_area) {
		try {
			if(Dist_area == null) {
				System.out.println("update employee set working_hours = "+hours + " where id = "+id);
				connectDB();
				ExecuteStatement("update employee set working_hours = "+hours + " where id = "+id+";");
			}
			else {
				System.out.println("update delegate set working_hours = "+hours + " where id = "+id);
				connectDB();
				ExecuteStatement("update delegate set working_hours = "+hours + " where id = "+id+";");

			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updatePhone(int id, int phone, String Dist_area) {
		try {
			if(Dist_area == null) {
				System.out.println("update employee set phone_num = "+phone + " where id = "+id);
				connectDB();
				ExecuteStatement("update employee set phone_num = "+phone + " where id = "+id+";");
			}
			else {
				System.out.println("update delegate set phone_num = "+phone + " where id = "+id);
				connectDB();
				ExecuteStatement("update delegate set phone_num = "+phone + " where id = "+id+";");
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateArea(int id, String area) {	
		try {
			System.out.println("update delegate set ename = '"+area + "' where id = "+id);
			connectDB();
			ExecuteStatement("update delegate set ename='"+area+"' where id="+id+";");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	void Alert(String message) {
		javafx.scene.control.Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(message);
		alert.setTitle("Error!");
		alert.setHeaderText(null);
		alert.setResizable(false);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.show();
	}
}

