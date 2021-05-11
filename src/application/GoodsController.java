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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableView;

public class GoodsController implements Initializable{
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
	private TextField search;

	@FXML
	private TableView<Goods> goods_table;

	@FXML
	private TableColumn<Goods, Integer> id_col;

	@FXML
	private TableColumn<Goods, String> name_col;

	@FXML
	private TableColumn<Goods, String> comp_col;

	@FXML
	private TableColumn<Goods, String> type_col;

	@FXML
	private TableColumn<Goods, Double> wholesale_col;

	@FXML
	private TableColumn<Goods, Double> retail_col;

	@FXML
	private TableColumn<Goods, Integer> count_col;

	@FXML
	private TableColumn<Goods, String> storage_col;

	@FXML
	private TableColumn<Goods, Date> exDate_col;

	@FXML
	private TextField id;

	@FXML
	private TextField name;

	@FXML
	private TextField comp;

	@FXML
	private TextField type;

	@FXML
	private TextField wholesale;

	@FXML
	private TextField count;

	@FXML
	private TextField retail;

	@FXML
	private TextField storage;

	@FXML
	private DatePicker exDate;

	@FXML
	private Button add_button;

	@FXML
	private Button delete_button;

	@FXML
	private Button search_button;

	@FXML
	private Button refresh_button;

	@FXML
	private TextField ClientOrder;

	@FXML
	private TextField CountOrder;

	@FXML
	private Button AddOrder;

	@FXML
	private TextField OrderNum;

	@FXML
	private Button confirm;

	@FXML
	private ComboBox<String> searchBy = new ComboBox<>();
	ObservableList<String> combobox = FXCollections.observableArrayList("Product ID", "Product Name", "Supplying Company",
			"Type", "Storage Location");
	private ArrayList<Goods> goods_data;
	private ObservableList<Goods> goods_list;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		goods_data = new ArrayList<>();
		try {
			getdata();
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		id_col.setCellValueFactory(new PropertyValueFactory<Goods, Integer>("product_id"));

		name_col.setCellValueFactory(new PropertyValueFactory<Goods, String>("product_name"));
		name_col.setCellFactory(TextFieldTableCell.<Goods>forTableColumn());
		name_col.setOnEditCommit(  
				(CellEditEvent<Goods, String> t) -> {
					((Goods) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setProduct_name(t.getNewValue());
					updateName( t.getRowValue().getProduct_id(),t.getNewValue());
				});
		comp_col.setCellValueFactory(new PropertyValueFactory<Goods, String>("supp_comp"));
		comp_col.setCellFactory(TextFieldTableCell.<Goods>forTableColumn());
		comp_col.setOnEditCommit(  
				(CellEditEvent<Goods, String> t) -> {
					((Goods) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setSupp_comp(t.getNewValue());
					updateSComp( t.getRowValue().getProduct_id(),t.getNewValue());
				});
		type_col.setCellValueFactory(new PropertyValueFactory<Goods, String>("ptype"));
		type_col.setCellFactory(TextFieldTableCell.<Goods>forTableColumn());
		type_col.setOnEditCommit(  
				(CellEditEvent<Goods, String> t) -> {
					((Goods) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setPtype(t.getNewValue());
					updateType( t.getRowValue().getProduct_id(),t.getNewValue());
				});
		wholesale_col.setCellValueFactory(new PropertyValueFactory<Goods, Double>("wholesale_price")); 
		wholesale_col.setCellFactory(TextFieldTableCell.<Goods,Double>forTableColumn(new DoubleStringConverter()));
		wholesale_col.setOnEditCommit(  
				(CellEditEvent<Goods, Double> t) -> {
					((Goods) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setWholesale_price(t.getNewValue());
					updateWholesale( t.getRowValue().getProduct_id(),t.getNewValue());
				});
		retail_col.setCellValueFactory(new PropertyValueFactory<Goods, Double>("retail_price")); 
		retail_col.setCellFactory(TextFieldTableCell.<Goods,Double>forTableColumn(new DoubleStringConverter()));
		retail_col.setOnEditCommit(  
				(CellEditEvent<Goods, Double> t) -> {
					((Goods) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setRetail_price(t.getNewValue());
					updateRetail( t.getRowValue().getProduct_id(),t.getNewValue());
				});
		count_col.setCellValueFactory(new PropertyValueFactory<Goods, Integer>("goods_count")); 
		count_col.setCellFactory(TextFieldTableCell.<Goods,Integer>forTableColumn(new IntegerStringConverter()));
		count_col.setOnEditCommit(  
				(CellEditEvent<Goods, Integer> t) -> {
					((Goods) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setGoods_count(t.getNewValue());
					updateCount( t.getRowValue().getProduct_id(),t.getNewValue());
				});
		storage_col.setCellValueFactory(new PropertyValueFactory<Goods, String>("storage_location"));
		storage_col.setCellFactory(TextFieldTableCell.<Goods>forTableColumn());
		storage_col.setOnEditCommit(  
				(CellEditEvent<Goods, String> t) -> {
					((Goods) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setStorage_location(t.getNewValue());
					updateStorage( t.getRowValue().getProduct_id(),t.getNewValue());
				});
		exDate_col.setCellValueFactory(new PropertyValueFactory<Goods, Date>("exp_date"));

		goods_table.setItems(get(goods_data));
		searchBy.setItems(combobox);
	}

	private ObservableList<Goods> get(ArrayList<Goods> c) {
		goods_list = FXCollections.observableArrayList();

		for (int i = 0; i < c.size(); i++) {

			if(c.get(i) != null)
				goods_list.add(c.get(i));
		}
		return goods_list;
	}

	private void getdata() throws ClassNotFoundException, SQLException {
		String SQL = "select * from goods";
		connectDB();
		System.out.println("Connection established");
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		while ( rs.next() ) {
			System.out.println(rs.getString(1)+ " "+ rs.getString(2) + " "+rs.getString(3) + " " + rs.getString(4)+
					" " + rs.getString(5)+" " + rs.getString(6)+" " + rs.getString(7)+" " + rs.getString(8)+" " + rs.getString(9));
			goods_data.add(new Goods(
					rs.getString(1),
					Integer.parseInt(rs.getString(2)),
					rs.getString(3),
					rs.getString(4),
					Double.parseDouble(rs.getString(5)),
					Double.parseDouble(rs.getString(6)),
					Integer.parseInt(rs.getString(7)),
					rs.getString(8),
					Date.valueOf(rs.getString(9))));
		}
		rs.close();
		con.close();
		System.out.println("Connection closed " + goods_data.size());
	}

	private void getCount() throws ClassNotFoundException, SQLException {
		connectDB();
		String SQL = "select order_num from orders order by 1 desc;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		int num=1;
		while(rs.next()) {
			num = Integer.parseInt(rs.getString(1))+1;
			break;
		}
		OrderNum.setText(String.valueOf(num));
		stmt.close();
		con.close();
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
	void addGoods(ActionEvent event) throws NumberFormatException, ClassNotFoundException, SQLException {
		if(!id.getText().matches("[0-9]+") || !wholesale.getText().matches("[0-9]+") || !retail.getText().matches("[0-9]+") ||
				!count.getText().matches("[0-9]+") || !(exDate.getValue() != null) || !(name.getText().length() > 0) ||
				!(type.getText().length() > 0) || !(comp.getText().length() > 0) || !(storage.getText().length() > 0)) {
			Alert("Check your input fields");
		}
		else if(dataExist(Integer.parseInt(id.getText()),name.getText(),comp.getText(),storage.getText(),type.getText(),
				Double.parseDouble(wholesale.getText()),Double.parseDouble(retail.getText()),Date.valueOf(exDate.getValue()))) {
			String SQL = "update goods set goods_count = goods_count+"+Integer.parseInt(count.getText())+" where product_id="+Integer.parseInt(id.getText())+";";
			connectDB();
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL);
			stmt.close();
			goods_table.refresh();
			comp.clear();
			id.clear();
			name.clear();
			type.clear();
			wholesale.clear();
			retail.clear();
			count.clear();
			storage.clear();
			exDate.getEditor().clear();
		}
		else if(!idExist(Integer.parseInt(id.getText())) && nameExist(name.getText()) &&
				!exDateExist(Integer.parseInt(id.getText()), name.getText(), Date.valueOf(exDate.getValue()))){
			Goods goods = new Goods(
					comp.getText(),
					Integer.parseInt(id.getText()),
					name.getText(),
					type.getText(),
					Double.parseDouble(wholesale.getText()),
					Double.parseDouble(retail.getText()),
					Integer.parseInt(count.getText()),
					storage.getText(),
					Date.valueOf(exDate.getValue()));
			goods_list.add(goods);
			insertData(goods);
			comp.clear();
			id.clear();
			name.clear();
			type.clear();
			wholesale.clear();
			retail.clear();
			count.clear();
			storage.clear();
			exDate.getEditor().clear();
			goods_table.refresh();
		}
		else if(idExist(Integer.parseInt(id.getText()))){
			Alert("Product ID Already Exists!");
		}
		else if(nameExist(name.getText())){
			Alert("Product Name Already Exists!");
		}
		else if(compExist(name.getText())){
			Alert("Supplying Company doesn't Exist!");
		}
		else if(storageExist(name.getText())){
			Alert("Storage Location doesn't Exist!");
		}
		else {
			Goods goods = new Goods(
					comp.getText(),
					Integer.parseInt(id.getText()),
					name.getText(),
					type.getText(),
					Double.parseDouble(wholesale.getText()),
					Double.parseDouble(retail.getText()),
					Integer.parseInt(count.getText()),
					storage.getText(),
					Date.valueOf(exDate.getValue()));
			goods_list.add(goods);
			insertData(goods);
			comp.clear();
			id.clear();
			name.clear();
			type.clear();
			wholesale.clear();
			retail.clear();
			count.clear();
			storage.clear();
			exDate.getEditor().clear();
		}
	}

	private boolean idExist(int id) throws ClassNotFoundException, SQLException {
		String SQL = "select * from goods where product_id="+id+";";
		connectDB();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		if(rs.last()) 
			return true;
		return false;
	}

	private boolean nameExist(String name) throws ClassNotFoundException, SQLException {
		String SQL = "select * from goods where product_name='"+name+"';";
		connectDB();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		if(rs.last()) 
			return true;
		return false;
	}

	private boolean compExist(String name) throws ClassNotFoundException, SQLException {
		String SQL = "select * from supplying_company where company_name='"+name+"';";
		connectDB();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		if(rs.getRow() == 1) 
			return true;
		return false;
	}

	private boolean storageExist(String storage) throws ClassNotFoundException, SQLException{
		String SQL = "select * from company where storage_locations='"+storage+"';";
		connectDB();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		if(rs.getRow() == 1) 
			return true;
		return false;
	}

	private boolean dataExist(int id, String name, String comp, String storage, String type, double wholesale, 
			double retail, Date date) throws ClassNotFoundException, SQLException {
		String SQL = "select * from goods where product_id="+id+" and product_name='"+name+"' and supp_comp='"+comp+"' and ptype='"+type+
				"' and storage_location='"+storage+"' and wholesale_price="+wholesale+" and retail_price="+retail+
				" and exp_date='"+date+"';";
		connectDB();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		if(rs.last()) 
			return true;
		return false;
	}

	private boolean exDateExist(int id, String name, Date date ) throws ClassNotFoundException, SQLException{
		String SQL = "select * from goods where exp_date='"+date+"' and product_id="+id+" and product_name='"+name+"';";
		connectDB();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		if(rs.last()) 
			return true;
		return false;
	}

	@FXML
	void deleteGoods(ActionEvent event) {
		ObservableList<Goods> selectedRows = goods_table.getSelectionModel().getSelectedItems();
		ArrayList<Goods> rows = new ArrayList<>(selectedRows);
		rows.forEach(row -> {
			goods_table.getItems().remove(row); 
			deleteRow(row); 
			goods_table.refresh();
		}); 
	}

	@FXML
	void searchGoods(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
		connectDB();
		try {
			String SQL;
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			if(searchBy.getValue() == "Product ID") {
				if(!search.getText().matches("[0-9]+")) {
					Alert("Product ID must be an Integer!");
					search.clear();
				}
				else {
					goods_list.clear();
					SQL = "select * from goods where product_id="+Integer.parseInt(search.getText())+";";
					rs = stmt.executeQuery(SQL);
					while(rs.next())
						goods_list.add(new Goods(rs.getString(1),Integer.parseInt(rs.getString(2)),rs.getString(3),rs.getString(4),
								Double.parseDouble(rs.getString(5)),Double.parseDouble(rs.getString(6)),Integer.parseInt(rs.getString(7)),
								rs.getString(8),Date.valueOf(rs.getString(9))));
				}
			}
			else if(searchBy.getValue() == "Product Name") {
				if(!(search.getText().length() > 0)) {
					Alert("Please Enter Product Name you want to Search for!");
					search.clear();
				}
				else {
					goods_list.clear();
					SQL = "select * from goods where product_name='"+search.getText()+"';";
					rs = stmt.executeQuery(SQL);
					while(rs.next())
						goods_list.add(new Goods(rs.getString(1),Integer.parseInt(rs.getString(2)),rs.getString(3),rs.getString(4),
								Double.parseDouble(rs.getString(5)),Double.parseDouble(rs.getString(6)),Integer.parseInt(rs.getString(7)),
								rs.getString(8),Date.valueOf(rs.getString(9))));
				}
			}
			else if(searchBy.getValue() == "Supplying Company") {
				if(!(search.getText().length() > 0)) {
					Alert("Please Enter Supplying Company you want to Search for!");
					search.clear();
				}
				else {
					goods_list.clear();
					SQL = "select * from goods where supp_comp='"+search.getText()+"';";
					rs = stmt.executeQuery(SQL);
					while(rs.next())
						goods_list.add(new Goods(rs.getString(1),Integer.parseInt(rs.getString(2)),rs.getString(3),rs.getString(4),
								Double.parseDouble(rs.getString(5)),Double.parseDouble(rs.getString(6)),Integer.parseInt(rs.getString(7)),
								rs.getString(8),Date.valueOf(rs.getString(9))));
				}
			}
			else if(searchBy.getValue() == "Type") {
				if(!(search.getText().length() > 0)) {
					Alert("Please Enter the Type you want to Search for!");
					search.clear();
				}
				else {
					goods_list.clear();
					SQL = "select * from goods where ptype='"+search.getText()+"';";
					rs = stmt.executeQuery(SQL);
					while(rs.next())
						goods_list.add(new Goods(rs.getString(1),Integer.parseInt(rs.getString(2)),rs.getString(3),rs.getString(4),
								Double.parseDouble(rs.getString(5)),Double.parseDouble(rs.getString(6)),Integer.parseInt(rs.getString(7)),
								rs.getString(8),Date.valueOf(rs.getString(9))));
				}
			}
			else if(searchBy.getValue() == "Storage Location") {
				if(!(search.getText().length() > 0)) {
					Alert("Please Enter the Storage Location you want to Search for!");
					search.clear();
				}
				else {
					goods_list.clear();
					SQL = "select * from goods where storage_location='"+search.getText()+"';";
					rs = stmt.executeQuery(SQL);
					while(rs.next())
						goods_list.add(new Goods(rs.getString(1),Integer.parseInt(rs.getString(2)),rs.getString(3),rs.getString(4),
								Double.parseDouble(rs.getString(5)),Double.parseDouble(rs.getString(6)),Integer.parseInt(rs.getString(7)),
								rs.getString(8),Date.valueOf(rs.getString(9))));
				}
			}
			else {
				Alert("Please Specify the Search By Box!");
			}
			rs.close();
			con.close();
		}catch(Exception EX) {
			System.out.println(EX);
		}
	}

	@FXML
	void refreshGoods(ActionEvent event) throws ClassNotFoundException, SQLException {
		goods_list.clear();
		search.clear();
		id_col.setCellValueFactory(new PropertyValueFactory<Goods, Integer>("product_id"));
		name_col.setCellValueFactory(new PropertyValueFactory<Goods, String>("product_name"));
		comp_col.setCellValueFactory(new PropertyValueFactory<Goods, String>("supp_comp"));
		type_col.setCellValueFactory(new PropertyValueFactory<Goods, String>("ptype"));
		wholesale_col.setCellValueFactory(new PropertyValueFactory<Goods, Double>("wholesale_price")); 
		retail_col.setCellValueFactory(new PropertyValueFactory<Goods, Double>("retail_price")); 
		count_col.setCellValueFactory(new PropertyValueFactory<Goods, Integer>("goods_count")); 
		storage_col.setCellValueFactory(new PropertyValueFactory<Goods, String>("storage_location"));
		exDate_col.setCellValueFactory(new PropertyValueFactory<Goods, Date>("exp_date"));
		goods_table.setItems(get(goods_data));
		searchBy.setItems(combobox);
		ClientOrder.clear();
		CountOrder.clear();
		comp.clear();
		id.clear();
		name.clear();
		type.clear();
		wholesale.clear();
		retail.clear();
		count.clear();
		storage.clear();
		exDate.getEditor().clear();
		getCount();
	}

	private void insertData(Goods goods) {
		try {
			System.out.println("Insert into goods values ('"+goods.getSupp_comp()+"',"+goods.getProduct_id()+",'"+goods.getProduct_name()+"'"
					+ ",'"+ goods.getPtype() +"',"+goods.getWholesale_price()+","+ goods.getRetail_price()+","+goods.getGoods_count()+",'"+goods.getStorage_location()+"','"+goods.getExp_date()+"');");
			connectDB();
			ExecuteStatement("Insert into goods values ('"+goods.getSupp_comp()+"',"+goods.getProduct_id()+",'"+goods.getProduct_name()+"'"
					+ ",'"+ goods.getPtype() +"',"+goods.getWholesale_price()+","+ goods.getRetail_price()+","+goods.getGoods_count()+",'"+goods.getStorage_location()+"','"+goods.getExp_date()+"');");
			con.close();
			System.out.println("Connection closed" + goods_data.size());
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

	private void deleteRow(Goods row) {
		try {
			System.out.println("delete from goods where product_id="+row.getProduct_id() + ";");
			connectDB();
			ExecuteStatement("delete from goods where product_id="+row.getProduct_id()+ ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateName(int id, String name) {	
		try {
			if(nameExist(name))
				Alert("Product Name Already Exists!");
			else {
				System.out.println("update goods set product_name = '"+name + "' where product_id = "+id);
				connectDB();
				ExecuteStatement("update goods set product_name='"+name+"' where product_id="+id+";");
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateSComp(int id, String supp_comp) {	
		try {
			if(!compExist(supp_comp)) {
				Alert("Supplying Company doesn't Exist!");
			}
			else {
				System.out.println("update goods set supp_comp = '"+supp_comp + "' where product_id = "+id);
				connectDB();
				ExecuteStatement("update goods set supp_comp='"+supp_comp+"' where product_id="+id+";");
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateType(int id, String type) {	
		try {
			System.out.println("update goods set ptype = '"+type + "' where product_id = "+id);
			connectDB();
			ExecuteStatement("update goods set ptype='"+type+"' where product_id="+id+";");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateWholesale(int id, Double wholesale) {	
		try {
			System.out.println("update goods set wholesale_price = '"+wholesale + "' where product_id = "+id);
			connectDB();
			ExecuteStatement("update goods set wholesale_price='"+wholesale+"' where product_id="+id+";");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateRetail(int id, Double retail) {	
		try {
			System.out.println("update goods set retail_price = '"+retail + "' where product_id = "+id);
			connectDB();
			ExecuteStatement("update goods set retail_price='"+retail+"' where product_id="+id+";");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateCount(int id, int count) {	
		try {
			System.out.println("update goods set goods_count = '"+count + "' where product_id = "+id);
			connectDB();
			ExecuteStatement("update goods set goods_count='"+count+"' where product_id="+id+";");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateStorage(int id, String storage) {	
		try {
			if(!storageExist(storage)){
				Alert("Storage Location doesn't Exist!");
			}
			else {
				System.out.println("update goods set storage_location = '"+storage + "' where product_id = "+id);
				connectDB();
				ExecuteStatement("update goods set storage_location='"+storage+"' where product_id="+id+";");
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void getOrderNum(MouseEvent event) throws ClassNotFoundException, SQLException {
		getCount();
	}

	@FXML
	void AddOrder(ActionEvent event) throws ClassNotFoundException, SQLException {
		if(!ClientOrder.getText().matches("[0-9]+") || !CountOrder.getText().matches("[0-9]+") || 
				!OrderNum.getText().matches("[0-9]+")) {
			Alert("Check Input Fieldes!");
			ClientOrder.clear();
			CountOrder.clear();
		}
		else {
			int num = Integer.parseInt(OrderNum.getText());	
			String SQL = "select * from bill where order_num="+num+" and isPaid = 1;";
			connectDB();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			if(rs.last()) {
				Alert("Cannot take this order number!");
			}
			else {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
				LocalDateTime now = LocalDateTime.now();  
				Date date = Date.valueOf(dtf.format(now));	
				ObservableList<Goods> selectedRows = goods_table.getSelectionModel().getSelectedItems();
				ArrayList<Goods> rows = new ArrayList<>(selectedRows);
				rows.forEach(row -> {
					goods_table.getItems();
					if(Integer.parseInt(CountOrder.getText()) <= row.getGoods_count()) {
						OrderLine order = new OrderLine(
								num,
								row.getProduct_id(),
								Integer.parseInt(ClientOrder.getText()),
								row.getProduct_name(),
								Integer.parseInt(CountOrder.getText()),
								row.getRetail_price(),
								date);
						insertOrder(order);
						int count = row.getGoods_count() - Integer.parseInt(CountOrder.getText());
						row.setGoods_count(count);
						goods_table.refresh();
						updateCount(row.getProduct_id(), count);
					}
					else
						Alert("Product Count Not Available!!");
				});
				CountOrder.clear();
			}
		}
	}

	private void insertOrder(OrderLine order) {
		try {
			connectDB();
			String SQL = "select order_num from orders where order_num="+order.getOrder_num()+";";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			rs.last();
			if(rs.getRow() == 0) {
				System.out.println("insert into orders values("+order.getOrder_num()+","+order.getCid()+",'"+order.getOrder_date()+"');");
				System.out.println("Insert into order_line values("+order.getOrder_num()+","+order.getProduct_id()+
						",'"+ order.getProduct_name() +"',"+order.getProduct_count()+","+order.getPrice()+");");
				ExecuteStatement("insert into orders values("+order.getOrder_num()+","+order.getCid()+",'"+order.getOrder_date()+"');");
				ExecuteStatement("Insert into order_line values("+order.getOrder_num()+","+order.getProduct_id()+
						",'"+ order.getProduct_name() +"',"+order.getProduct_count()+","+order.getPrice()+");");
			}
			else {
				System.out.println("Insert into order_line values("+order.getOrder_num()+","+order.getProduct_id()+
						",'"+ order.getProduct_name() +"',"+order.getProduct_count()+","+order.getPrice()+");");
				ExecuteStatement("Insert into order_line values("+order.getOrder_num()+","+order.getProduct_id()+
						",'"+ order.getProduct_name() +"',"+order.getProduct_count()+","+order.getPrice()+");");
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void confirmOrder(ActionEvent event) throws ClassNotFoundException, SQLException {
		CountOrder.clear();
		ClientOrder.clear();
		getCount();
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


