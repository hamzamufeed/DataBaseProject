package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class ClientController implements Initializable{
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
	private TableView<Client> client_table;

	@FXML
	private TableColumn<Client, Integer> id_col;

	@FXML
	private TableColumn<Client, String> name_col;

	@FXML
	private TableColumn<Client, Integer> delegateID_col;

	@FXML
	private TableColumn<Client, String> area_col;

	@FXML
	private TableColumn<Client, Integer> phone_col;

	@FXML
	private TableColumn<Client, Double> debt_col;

	@FXML
	private TextField id;

	@FXML
	private TextField name;

	@FXML
	private TextField delegate_id;

	@FXML
	private TextField area;

	@FXML
	private TextField phone;

	@FXML
	private Button add_button;

	@FXML
	private Button delete_button;

	@FXML
	private TextField search;

	@FXML
	private Button search_button;

	@FXML
	private Button refresh_button;

	private ArrayList<Client> client_data;
	private ObservableList<Client> client_list;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		client_data = new ArrayList<>();
		try {
			getdata();

		}catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		id_col.setCellValueFactory(new PropertyValueFactory<Client, Integer>("id"));

		name_col.setCellValueFactory(new PropertyValueFactory<Client,String>("cname"));
		name_col.setCellFactory(TextFieldTableCell.<Client>forTableColumn());
		name_col.setOnEditCommit(  
				(CellEditEvent<Client, String> t) -> {
					((Client) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setCname(t.getNewValue());
					updateName( t.getRowValue().getId(),t.getNewValue());
				});
		area_col.setCellValueFactory(new PropertyValueFactory<Client,String>("area"));
		area_col.setCellFactory(TextFieldTableCell.<Client>forTableColumn());
		area_col.setOnEditCommit(  
				(CellEditEvent<Client, String> t) -> {
					((Client) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setArea(t.getNewValue());
					updateArea( t.getRowValue().getId(),t.getNewValue());
				});
		phone_col.setCellValueFactory(new PropertyValueFactory<Client, Integer>("phone_num"));
		phone_col.setCellFactory(TextFieldTableCell.<Client,Integer>forTableColumn(new IntegerStringConverter()));
		phone_col.setOnEditCommit(        
				(CellEditEvent<Client, Integer> t) -> {
					((Client) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setPhone_num(t.getNewValue());
					updatePhone( t.getRowValue().getId(),t.getNewValue());
				});
		delegateID_col.setCellValueFactory(new PropertyValueFactory<Client, Integer>("delegate_id"));
		delegateID_col.setCellFactory(TextFieldTableCell.<Client,Integer>forTableColumn(new IntegerStringConverter()));
		delegateID_col.setOnEditCommit(        
				(CellEditEvent<Client, Integer> t) -> {
					((Client) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setDelegate_id(t.getNewValue());
					updateDelegateID( t.getRowValue().getId(),t.getNewValue());
				});
		debt_col.setCellValueFactory(new PropertyValueFactory<Client, Double>("debt"));
		debt_col.setCellFactory(TextFieldTableCell.<Client,Double>forTableColumn(new DoubleStringConverter()));
		debt_col.setOnEditCommit(  
				(CellEditEvent<Client, Double> t) -> {
					((Client) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setDebt(t.getNewValue());
					updateDebt( t.getRowValue().getId(),t.getNewValue());
				});
		client_table.setItems(get(client_data));
	}

	private ObservableList<Client> get(ArrayList<Client> c) {
		client_list = FXCollections.observableArrayList();

		for (int i = 0; i < c.size(); i++) {

			if(c.get(i) != null)
				client_list.add(c.get(i));
		}
		return client_list;
	}

	private void getdata() throws ClassNotFoundException, SQLException {
		String SQL = "select * from clients";
		connectDB();
		System.out.println("Connection established");
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		while ( rs.next()) {
			System.out.println(rs.getString(1)+ " "+ rs.getString(2) + " "+rs.getString(3) +
					" " + rs.getString(4)+" " + rs.getString(5)+" " + rs.getString(6));
			Client client = new Client(
					Integer.parseInt(rs.getString(1)),
					rs.getString(2),
					rs.getString(3),
					Integer.parseInt(rs.getString(4)),
					Integer.parseInt(rs.getString(5)));	
			client.setDebt(Double.parseDouble(rs.getString(6)));
			client_data.add(client);
		}
		rs.close();
		con.close();
		System.out.println("Connection closed " + client_data.size());
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

	@FXML
	void deleteClient(ActionEvent event) {
		ObservableList<Client> selectedRows = client_table.getSelectionModel().getSelectedItems();
		ArrayList<Client> rows = new ArrayList<>(selectedRows);
		rows.forEach(row -> { 
			deleteRow(row); 
			client_table.refresh();

		});
	}

	@FXML
	void addClient(ActionEvent event) throws NumberFormatException, ClassNotFoundException, SQLException {
		if(!id.getText().matches("[0-9]+") || !delegate_id.getText().matches("[0-9]+") || !phone.getText().matches("[0-9]+") ||
				!(name.getText().length() > 0) || !(area.getText().length() > 0)) {
			Alert("Check your input fields");
		}
		else if(idExist(Integer.parseInt(id.getText()))) {
			Alert("Client ID Already Exists!");
		}
		else if(!delegateExist(Integer.parseInt(delegate_id.getText()))) {
			Alert("Delegate ID Doesn't Exist!");
		}
		else {
			Client client = new Client(
					Integer.parseInt(id.getText()),
					name.getText(),
					area.getText(),
					Integer.parseInt(delegate_id.getText()),
					Integer.parseInt(phone.getText()));
			client_list.add(client);
			insertData(client);
			id.clear();
			name.clear();
			area.clear();
			delegate_id.clear();
			phone.clear();
		}
	}

	private boolean idExist(int id) throws ClassNotFoundException, SQLException {
		String SQL = "select * from clients where cid="+id+";";
		connectDB();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		if(rs.last()) 
			return true;
		return false;
	}

	private boolean delegateExist(int id) throws ClassNotFoundException, SQLException {
		String SQL = "select * from delegate where id="+id+";";
		connectDB();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		if(rs.last()) 
			return true;
		return false;
	}

	@FXML
	void refreshClient(ActionEvent event) {
		client_list.clear();
		search.clear();
		id_col.setCellValueFactory(new PropertyValueFactory<Client, Integer>("id"));
		name_col.setCellValueFactory(new PropertyValueFactory<Client,String>("cname"));
		area_col.setCellValueFactory(new PropertyValueFactory<Client,String>("area"));
		phone_col.setCellValueFactory(new PropertyValueFactory<Client, Integer>("phone_num"));
		delegateID_col.setCellValueFactory(new PropertyValueFactory<Client, Integer>("delegate_id"));
		debt_col.setCellValueFactory(new PropertyValueFactory<Client, Double>("debt"));
		client_table.setItems(get(client_data));
	}

	@FXML
	void searchClient(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
		if(!search.getText().matches("[0-9]+"))
			Alert("Enter Client ID to Search");
		else {
			connectDB();
			try {
				client_list.clear();			
				String SQL = "select * from clients where cid="+Integer.parseInt(search.getText())+";";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(SQL);
				while ( rs.next()) {
					client_list.add(new Client(Integer.parseInt(rs.getString(1)),rs.getString(2),rs.getString(3),
							Integer.parseInt(rs.getString(4)),Integer.parseInt(rs.getString(5))));		
				}
				rs.close();
				con.close();
			}
			catch(Exception EX){
				System.out.print(EX);
			}
		}
	}

	private void insertData(Client client) {
		try {
			System.out.println("Insert into clients values ("+client.getId()+",'"+client.getCname()+"','"+client.getArea()+"'"
					+ ","+ client.getDelegate_id() +","+client.getPhone_num()+","+client.getDebt()+");");
			connectDB();
			ExecuteStatement("Insert into clients values ("+client.getId()+",'"+client.getCname()+"','"+client.getArea()+"'"
					+ ","+ client.getDelegate_id() +","+client.getPhone_num()+","+client.getDebt()+");");
			con.close();
			System.out.println("Connection closed" + client_data.size());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
	
	private void deleteRow(Client row) {
		try {
			if(row.getDebt() > 0)
				Alert("Can't Delete! This Client has Debts!");
			else {
				connectDB();
				String SQL = "select * from orders where cid="+row.getId()+";";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(SQL);
				if(rs.next() == true)
					Alert("Can't delete a Client who has registered Orders!");
				else {
					System.out.println("delete from clients where cid="+row.getId() + ";");
					connectDB();
					ExecuteStatement("delete from clients where cid="+row.getId()+ ";");
					client_table.getItems().remove(row);
				}	
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateName(int id, String name) {	
		try {
			System.out.println("update clients set cname = '"+name + "' where cid = "+id);
			connectDB();
			ExecuteStatement("update clients set cname='"+name+"' where cid="+id+";");
			con.close();
			System.out.println("Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateArea(int id, String area) {	
		try {
			System.out.println("update clients set area = '"+area + "' where cid = "+id);
			connectDB();
			ExecuteStatement("update clients set area='"+area+"' where cid="+id+";");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updatePhone(int id, int phone) {
		try {
			System.out.println("update clients set phone_num = "+phone + " where cid = "+id);
			connectDB();
			ExecuteStatement("update clients set phone_num = "+phone + " where cid = "+id+";");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateDelegateID(int id, int delegate_id) {
		try {
			if(!delegateExist(delegate_id))
				Alert("Delegate ID doesn't Exits!");
			else {
				System.out.println("update clients set delegate_id = "+delegate_id + " where cid = "+id);
				connectDB();
				ExecuteStatement("update clients set delegate_id = "+delegate_id + " where cid = "+id+";");
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateDebt(int id, double debt) {
		try {
			System.out.println("update clients set debt = "+debt + " where cid = "+id);
			connectDB();
			ExecuteStatement("update clients set debt = "+debt + " where cid = "+id+";");
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

