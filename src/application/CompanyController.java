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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;

public class CompanyController implements Initializable{
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
	private TableView<SupplyingCompany> table1;

	@FXML
	private TableColumn<SupplyingCompany, String> SComp;

	@FXML
	private TableColumn<SupplyingCompany, Double> FRecord;

	@FXML
	private TableView<Company> table2;

	@FXML
	private TableColumn<Company, String> Storage;

	@FXML
	private Label lable;

	@FXML
	private TextField count;

	@FXML
	private TextField text2;

	@FXML
	private Button delete1;

	@FXML
	private Button add2;

	@FXML
	private TextField text1;

	@FXML
	private Button add1;

	@FXML
	private Button delete2;

	private ArrayList<SupplyingCompany> SComp_data;
	private ObservableList<SupplyingCompany> SComp_list;
	private ArrayList<Company> Company_data;
	private ObservableList<Company> Company_list;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		SComp_data = new ArrayList<>();
		Company_data = new ArrayList<>();
		try {
			getdata();		
			getFRecord();
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		SComp.setCellValueFactory(new PropertyValueFactory<SupplyingCompany, String>("name"));
		FRecord.setCellValueFactory(new PropertyValueFactory<SupplyingCompany, Double>("financial_value"));
		table1.setItems(get1(SComp_data));
		Storage.setCellValueFactory(new PropertyValueFactory<Company, String>("storage_location"));
		table2.setItems(get2(Company_data));
		getCount();
	}

	void getdata() throws SQLException, ClassNotFoundException {
		connectDB();
		System.out.println("Connection established");
		String SQL1 = "select * from supplying_company";
		Statement stmt1 = con.createStatement();
		ResultSet rs1 = stmt1.executeQuery(SQL1);

		while ( rs1.next() ) {
			SComp_data.add(new SupplyingCompany(rs1.getString(1),Double.parseDouble(rs1.getString(2))));
			ExecuteStatement("update supplying_company set financial_value= 0 where company_name='"+rs1.getString(1)+"';");
		}
		rs1.close();

		String SQL2 = "select * from company";
		Statement stmt2 = con.createStatement();
		ResultSet rs2 = stmt2.executeQuery(SQL2);
		while ( rs2.next() ) {
			Company_data.add(new Company(rs2.getString(1)));
		}
		rs2.close();
		con.close();
		System.out.println("Connection closed " + SComp_data.size()+", "+Company_data.size());
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

	public  ObservableList<SupplyingCompany> get1(ArrayList<SupplyingCompany> c){
		SComp_list = FXCollections.observableArrayList();

		for (int i = 0; i < c.size(); i++) {

			if(c.get(i) != null)
				SComp_list.add(c.get(i));
		}
		return SComp_list;
	}

	public  ObservableList<Company> get2(ArrayList<Company> c){
		Company_list = FXCollections.observableArrayList();

		for (int i = 0; i < c.size(); i++) {

			if(c.get(i) != null)
				Company_list.add(c.get(i));
		}
		return Company_list;
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

	@FXML
	void addCompany(ActionEvent event) {
		SupplyingCompany comp = new SupplyingCompany(text1.getText());
		insertCompany(comp);
		text1.clear();
	}

	@FXML
	void addStorage(ActionEvent event) {
		Company storage = new Company(text2.getText());
		insertLocation(storage);
		text2.clear();
	}

	@FXML
	void deleteCompany(ActionEvent event) {
		ObservableList<SupplyingCompany> selectedRows = table1.getSelectionModel().getSelectedItems();
		ArrayList<SupplyingCompany> rows = new ArrayList<>(selectedRows);
		rows.forEach(row -> {
			deleteRow(row); 
			table1.refresh();
		}); 
	}

	@FXML
	void deleteStorage(ActionEvent event) {
		ObservableList<Company> selectedRows = table2.getSelectionModel().getSelectedItems();
		ArrayList<Company> rows = new ArrayList<>(selectedRows);
		rows.forEach(row -> {
			deleteRow(row); 
			table2.refresh();
		});
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

	private void insertCompany(SupplyingCompany comp) {
		try {
			if(compExist(comp.getName()))
				Alert("Supplying Company Already Exists!");
			else {
				System.out.println("Insert into supplying_company values('"+comp.getName()+"',"+comp.getFinancial_value()+");");
				connectDB();
				ExecuteStatement("Insert into supplying_company values('"+comp.getName()+"',"+comp.getFinancial_value()+");");
				SComp_list.add(comp);
				con.close();
				System.out.println("Connection closed" + SComp_data.size());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void insertLocation(Company storage) {
		try {
			if(storageExist(storage.getStorage_location()))
				Alert("Storage Location Already Exists!");
			else {
				System.out.println("Insert into company values('"+storage.getStorage_location()+"'"+");");
				connectDB();
				ExecuteStatement("Insert into company values('"+storage.getStorage_location()+"'"+");");
				Company_list.add(storage);
				con.close();
				System.out.println("Connection closed" + Company_data.size());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private boolean compExist(String name) throws ClassNotFoundException, SQLException {
		String SQL = "select * from supplying_company where company_name='"+name+"';";
		connectDB();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		if(rs.last()) 
			return true;
		return false;
	}

	private boolean storageExist(String storage) throws ClassNotFoundException, SQLException{
		String SQL = "select * from company where storage_locations='"+storage+"';";
		connectDB();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		if(rs.last()) 
			return true;
		return false;
	}

	private void deleteRow(SupplyingCompany row) {
		try {
			connectDB();
			String SQL = "select * from goods where supp_comp='"+row.getName()+"';";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			if(rs.next() == true)
				Alert("Couldn't Delete! There's Goods related to this Company");
			else {
				System.out.println("delete from supplying_company where company_name="+"'"+row.getName()+"'" + ";");
				ExecuteStatement("delete from supplying_company where company_name="+"'"+row.getName()+"'" + ";");
				table1.getItems().remove(row); 
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void deleteRow(Company row) {
		try {
			connectDB();
			String SQL = "select * from goods where storage_location='"+row.getStorage_location()+"';";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			if(rs.next() == true)
				Alert("Couldn't Delete! There's Goods in this Location");
			else {
				System.out.println("delete from company where storage_locations="+"'"+row.getStorage_location()+"'" + ";");
				ExecuteStatement("delete from company where storage_locations="+"'"+row.getStorage_location()+ "'"+ ";");
				table2.getItems().remove(row); 
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void getCount() {
		int num = 0;
		try {
			connectDB();
			String SQL = "SELECT * FROM employee;";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			rs.last();
			num = rs.getRow();
			SQL = "SELECT * FROM delegate;";
			rs = stmt.executeQuery(SQL);
			rs.last();
			num += rs.getRow();
			count.setText(String.valueOf(num));
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void getFRecord() {
		try {
			connectDB();
			String SQL = "SELECT * FROM goods;";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			while(rs.next()) {
				ExecuteStatement("update supplying_company set financial_value=financial_value+"
						+Double.parseDouble(rs.getString(5))*Integer.parseInt(rs.getString(7))+
						"where company_name='"+rs.getString(1)+"';");
			}
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
