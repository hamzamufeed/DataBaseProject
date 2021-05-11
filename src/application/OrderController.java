package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.time.LocalDateTime; 

public class OrderController implements Initializable{
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
	private TableView<OrderLine> orders_table;

	@FXML
	private TableColumn<OrderLine, Integer> orderNum_col;

	@FXML
	private TableColumn<OrderLine, Integer> clientID_col;

	@FXML
	private TableColumn<OrderLine, Integer> productID_col;

	@FXML
	private TableColumn<OrderLine, String> productName_col;

	@FXML
	private TableColumn<OrderLine, Integer> productCount_col;

	@FXML
	private TableColumn<OrderLine, Date> date_col;

	@FXML
	private TextField search_bar;

	@FXML
	private RadioButton search_client;

	@FXML
	private ToggleGroup Search;

	@FXML
	private RadioButton search_product;

	@FXML
	private Button delete;

	@FXML
	private Button refresh;

	@FXML
	private Button search;

	@FXML
	private Button getbill_button;

	@FXML
	private TextArea billDetails;

	@FXML
	private RadioButton paid;

	@FXML
	private ToggleGroup ConfirmBill;

	@FXML
	private RadioButton notPaid;

	@FXML
	private Button confirm;

	@FXML
	private Button save;

	private ArrayList<OrderLine> orderLine_data;
	private ObservableList<OrderLine> orderLine_list;
	private ArrayList<Bill> bill_data;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderLine_data = new ArrayList<>();
		bill_data = new ArrayList<>();
		try {
			getdata();

		}catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		orderNum_col.setCellValueFactory(new PropertyValueFactory<OrderLine, Integer>("order_num"));
		clientID_col.setCellValueFactory(new PropertyValueFactory<OrderLine,Integer>("cid"));
		productID_col.setCellValueFactory(new PropertyValueFactory<OrderLine,Integer>("product_id"));
		productName_col.setCellValueFactory(new PropertyValueFactory<OrderLine, String>("product_name"));
		productCount_col.setCellValueFactory(new PropertyValueFactory<OrderLine, Integer>("product_count"));
		productCount_col.setCellFactory(TextFieldTableCell.<OrderLine,Integer>forTableColumn(new IntegerStringConverter()));
		productCount_col.setOnEditCommit(  
				(CellEditEvent<OrderLine, Integer> t) -> {
					int current_count = ((OrderLine) t.getTableView().getItems().get(t.getTablePosition().getRow())).getProduct_count();
					((OrderLine) t.getTableView().getItems().get(
							t.getTablePosition().getRow())
							).setProduct_count(t.getNewValue());
					updateCount( t.getRowValue().getOrder_num(),t.getRowValue().getProduct_id(),current_count,t.getNewValue());
				});

		date_col.setCellValueFactory(new PropertyValueFactory<OrderLine, Date>("order_date"));
		orders_table.setItems(get(orderLine_data));
		confirm.setDisable(true);
		save.setDisable(true);
	}

	private ObservableList<OrderLine> get(ArrayList<OrderLine> c) {
		orderLine_list = FXCollections.observableArrayList();

		for (int i = 0; i < c.size(); i++) {

			if(c.get(i) != null)
				orderLine_list.add(c.get(i));
		}
		return orderLine_list;
	}

	private void getdata() throws ClassNotFoundException, SQLException {
		String SQL = "select * from orders O, order_line OL where O.order_num = OL.order_num order by 1;";
		connectDB();
		System.out.println("Connection established");
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		while ( rs.next()) {
			System.out.println(rs.getString(1)+" "+rs.getString(5)+" "+rs.getString(2)+" "+rs.getString(6)+" "+
					rs.getString(7)+" "+rs.getString(8)+" "+rs.getString(3));
			orderLine_data.add(new OrderLine(
					Integer.parseInt(rs.getString(1)),
					Integer.parseInt(rs.getString(5)),
					Integer.parseInt(rs.getString(2)),
					rs.getString(6),
					Integer.parseInt(rs.getString(7)),
					Double.parseDouble(rs.getString(8)),
					Date.valueOf(rs.getString(3))));
		}
		SQL = "SELECT * FROM bill;";
		stmt = con.createStatement();
		rs = stmt.executeQuery(SQL);
		while(rs.next()) {
			Bill bill = new Bill(
					Integer.parseInt(rs.getString(1)),
					Integer.parseInt(rs.getString(2)),
					Integer.parseInt(rs.getString(6)),
					Double.parseDouble(rs.getString(3)),
					Double.parseDouble(rs.getString(4)),
					Date.valueOf(rs.getString(5)));
			bill.setPaid(Boolean.parseBoolean(rs.getString(7)));
			bill_data.add(bill);
		}
		rs.close();
		con.close();
		System.out.println("Connection closed "+ orderLine_data.size());
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

	private void deleteRow(OrderLine row) {
		try {
			connectDB();
			String SQL = "select * from bill where order_num="+row.getOrder_num()+";";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			String SQL2 = "select * from order_line where order_num="+row.getOrder_num()+";";
			Statement stmt2 = con.createStatement();
			ResultSet rs2 = stmt2.executeQuery(SQL2);
			rs.last();
			if(rs.getRow() == 1) {
				System.out.println("delete from bill where order_num="+row.getOrder_num()+";");
				ExecuteStatement("delete from bill where order_num="+row.getOrder_num()+";");
				if(Integer.parseInt(rs.getString(7)) == 0) {
					System.out.println("update goods set goods_count = goods_count+"+row.getProduct_count()+" where product_id="+row.getProduct_id()+";");
					ExecuteStatement("update goods set goods_count = goods_count+"+row.getProduct_count()+" where product_id="+row.getProduct_id()+";");
					rs2.last();
					if(rs2.getRow() == 1) {
						System.out.println("delete from order_line where order_num="+row.getOrder_num() +" and product_id="+row.getProduct_id()+";");
						System.out.println("delete from orders where order_num="+row.getOrder_num()+";");
						ExecuteStatement("delete from order_line where order_num="+row.getOrder_num() + " and product_id="+row.getProduct_id()+";");		
						ExecuteStatement("delete from orders where order_num="+row.getOrder_num()+";");
					}	
					else {
						System.out.println("delete from order_line where order_num="+row.getOrder_num() +" and product_id="+row.getProduct_id()+";");
						ExecuteStatement("delete from order_line where order_num="+row.getOrder_num() + " and product_id="+row.getProduct_id()+";");
					}
				}
				else {
					rs2.last();
					if(rs2.getRow() == 1) {
						System.out.println("delete from order_line where order_num="+row.getOrder_num() +" and product_id="+row.getProduct_id()+";");
						System.out.println("delete from orders where order_num="+row.getOrder_num()+";");
						ExecuteStatement("delete from order_line where order_num="+row.getOrder_num() + " and product_id="+row.getProduct_id()+";");		
						ExecuteStatement("delete from orders where order_num="+row.getOrder_num()+";");
					}
					else {
						System.out.println("delete from order_line where order_num="+row.getOrder_num() +" and product_id="+row.getProduct_id()+";");
						ExecuteStatement("delete from order_line where order_num="+row.getOrder_num() + " and product_id="+row.getProduct_id()+";");
					}
				}
			}
			else {
				rs2.last();
				if(rs2.getRow() == 1) {
					System.out.println("update goods set goods_count = goods_count+"+row.getProduct_count()+" where product_id="+row.getProduct_id()+";");
					System.out.println("delete from order_line where order_num="+row.getOrder_num() +" and product_id="+row.getProduct_id()+";");
					System.out.println("delete from orders where order_num="+row.getOrder_num()+";");
					ExecuteStatement("update goods set goods_count = goods_count+"+row.getProduct_count()+" where product_id="+row.getProduct_id()+";");
					ExecuteStatement("delete from order_line where order_num="+row.getOrder_num() + " and product_id="+row.getProduct_id()+";");		
					ExecuteStatement("delete from orders where order_num="+row.getOrder_num()+";");
				}	
				else {
					System.out.println("update goods set goods_count = goods_count+"+row.getProduct_count()+" where product_id="+row.getProduct_id()+";");
					System.out.println("delete from order_line where order_num="+row.getOrder_num() +" and product_id="+row.getProduct_id()+";");
					ExecuteStatement("update goods set goods_count = goods_count+"+row.getProduct_count()+" where product_id="+row.getProduct_id()+";");
					ExecuteStatement("delete from order_line where order_num="+row.getOrder_num() + " and product_id="+row.getProduct_id()+";");
				}
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void deleteOrders(ActionEvent event) {
		ObservableList<OrderLine> selectedRows = orders_table.getSelectionModel().getSelectedItems();
		ArrayList<OrderLine> rows = new ArrayList<>(selectedRows);
		rows.forEach(row -> {
			orders_table.getItems().remove(row); 
			deleteRow(row); 
			orders_table.refresh();
		});
	}

	@FXML
	void refreshOrders(ActionEvent event) {
		orderLine_list.clear();
		search_bar.clear();
		billDetails.clear();
		orderNum_col.setCellValueFactory(new PropertyValueFactory<OrderLine, Integer>("order_num"));
		clientID_col.setCellValueFactory(new PropertyValueFactory<OrderLine,Integer>("cid"));
		productID_col.setCellValueFactory(new PropertyValueFactory<OrderLine,Integer>("product_id"));
		productName_col.setCellValueFactory(new PropertyValueFactory<OrderLine, String>("product_name"));
		productCount_col.setCellValueFactory(new PropertyValueFactory<OrderLine, Integer>("product_count"));
		date_col.setCellValueFactory(new PropertyValueFactory<OrderLine, Date>("order_date"));
		orders_table.setItems(get(orderLine_data));
	}

	@FXML
	void searchOrders(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
		connectDB();
		try {
			String SQL;
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			if(search_client.isSelected()) {
				orderLine_list.clear();
				SQL = "select * from orders O, order_line OL where O.cid="+Integer.parseInt(search_bar.getText())+
						" and O.order_num = OL.order_num ;";
				stmt = con.createStatement();
				rs = stmt.executeQuery(SQL);
				while ( rs.next()) {
					System.out.println(rs.getString(1)+" "+rs.getString(5)+" "+rs.getString(2)+" "+rs.getString(6)+" "+
							rs.getString(7)+" "+rs.getString(8)+" "+rs.getString(3));
					orderLine_list.add(new OrderLine(
							Integer.parseInt(rs.getString(1)),
							Integer.parseInt(rs.getString(5)),
							Integer.parseInt(rs.getString(2)),
							rs.getString(6),
							Integer.parseInt(rs.getString(7)),
							Double.parseDouble(rs.getString(8)),
							Date.valueOf(rs.getString(3))));		
				}
			}
			else if(search_product.isSelected()) {
				orderLine_list.clear();
				SQL = "select * from orders O, order_line OL where product_id="+Integer.parseInt(search_bar.getText())+
						" and O.order_num = OL.order_num ;";
				stmt = con.createStatement();
				rs = stmt.executeQuery(SQL);
				while ( rs.next()) {
					System.out.println(rs.getString(1)+" "+rs.getString(5)+" "+rs.getString(2)+" "+rs.getString(6)+" "+
							rs.getString(7)+" "+rs.getString(8)+" "+rs.getString(3));
					orderLine_list.add(new OrderLine(
							Integer.parseInt(rs.getString(1)),
							Integer.parseInt(rs.getString(5)),
							Integer.parseInt(rs.getString(2)),
							rs.getString(6),
							Integer.parseInt(rs.getString(7)),
							Double.parseDouble(rs.getString(8)),
							Date.valueOf(rs.getString(3))));	
				}
			}
			rs.close();
			con.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateCount(int order_num,int product_id, int current_count, int new_count) {
		int n = current_count - new_count;
		int product_count = 0;
		try {
			connectDB();
			String SQL2 = "select goods_count from goods where product_id="+product_id+";";
			Statement stmt2 = con.createStatement();
			ResultSet rs2 = stmt2.executeQuery(SQL2);
			while(rs2.next())
				product_count = Integer.parseInt(rs2.getString(1));
			if(product_count < new_count)
				Alert("Product Count not Available!");
			else {
				String SQL = "SELECT * FROM bill where order_num="+order_num+" ;";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(SQL);
				rs.last();
				if(rs.getRow() == 1) {
					if(Integer.parseInt(rs.getString(7)) == 0) {
						ExecuteStatement("update goods set goods_count = goods_count+"+n+" where product_id="+product_id+";");
						ExecuteStatement("update order_line set product_count="+new_count+" where order_num="+order_num+
								" and product_id="+product_id+";");
					}
					else
						Alert("Order is Already Paid!");
				}
				else {
					ExecuteStatement("update goods set goods_count = goods_count+"+n+" where product_id="+product_id+";");
					ExecuteStatement("update order_line set product_count="+new_count+" where order_num="+order_num+
							" and product_id="+product_id+";");
				}
			}
			con.close();
			orders_table.refresh();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	static int current_bill;
	@FXML
	void getBill(ActionEvent event) {
		ObservableList<OrderLine> selectedRows = orders_table.getSelectionModel().getSelectedItems();
		ArrayList<OrderLine> rows = new ArrayList<>(selectedRows);
		rows.forEach(row -> {
			orders_table.getItems();
			try {
				connectDB();
				String SQL = "SELECT * FROM bill where order_num="+row.getOrder_num()+" ;";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(SQL);
				rs.last();
				if(rs.getRow() == 0) {
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
					LocalDateTime now = LocalDateTime.now();  
					Date date = Date.valueOf(dtf.format(now));
					Bill bill = new Bill(
							getBillCount(),
							row.getCid(),
							row.getOrder_num(),
							getCashValue(row),
							getDebtValue(row),
							date);
					bill_data.add(bill);
					insertBill(bill);
					current_bill = bill.getBill_num();
					billDetails.setText(
							"Bill Number: "+String.valueOf(bill.getBill_num())+"\n"
									+"Client ID: "+String.valueOf(bill.getCid())+"\n"
									+"Order Number: "+String.valueOf(bill.getOrder_num())+"\n"
									+"Cash Value: "+String.valueOf(bill.getCash())+"\n"
									+"Debt Value: "+String.valueOf(bill.getDebt())+"\n"
									+"Date :"+String.valueOf(bill.getDate())+"\n"
									+"Paid: "+bill.isPaid());
					confirm.setDisable(false);
					save.setDisable(false);
				}
				else {
					current_bill = Integer.parseInt(rs.getString(1));
					billDetails.setText(
							"Bill Number: "+rs.getString(1)+"\n"
									+"Client ID: "+rs.getString(2)+"\n"
									+"Order Number: "+rs.getString(6)+"\n"
									+"Cash Value: "+rs.getString(3)+"\n"
									+"Debt Value: "+rs.getString(4)+"\n"
									+"Date: "+rs.getString(5)+"\n"
									+"Paid: "+rs.getString(7));
					save.setDisable(false);
					if(Integer.parseInt(rs.getString(7)) == 1)
						confirm.setDisable(true);
					else
						confirm.setDisable(false);
				}
				con.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	private void insertBill(Bill bill) {
		try {
			System.out.println("Insert into bill values ("+bill.getBill_num()+","+bill.getCid()+","+bill.getCash()+
					","+ bill.getDebt() +",'"+bill.getDate()+"',"+bill.getOrder_num()+","+bill.isPaid()+");");
			connectDB();
			ExecuteStatement("Insert into bill values ("+bill.getBill_num()+","+bill.getCid()+","+bill.getCash()+
					","+ bill.getDebt() +",'"+bill.getDate()+"',"+bill.getOrder_num()+","+bill.isPaid()+");");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private int getBillCount() throws ClassNotFoundException, SQLException {
		connectDB();
		String SQL = "select bill_num from bill order by 1 desc";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		int num=1;
		while(rs.next()) {
			num = Integer.parseInt(rs.getString(1))+1;
			break;
		}
		stmt.close();
		con.close();
		return num;
	}

	private double getCashValue(OrderLine row) throws ClassNotFoundException, SQLException {
		double cash = 0;
		connectDB();
		String SQL = "select retail_price, product_count from goods G, order_line O where O.order_num="+row.getOrder_num()+
				" and O.product_id = G.product_id ;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		while(rs.next()) {
			cash += Double.parseDouble(rs.getString(1))*Integer.parseInt(rs.getString(2));
		}
		return cash;
	}

	private double getDebtValue(OrderLine row) throws ClassNotFoundException, SQLException{
		double debt = 0;
		connectDB();
		String SQL = "select debt from clients where cid="+row.getCid()+";";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		while(rs.next()) {
			debt += Double.parseDouble(rs.getString(1));
		}
		return debt;
	}

	@FXML
	void confirmBill(ActionEvent event) {
		if(paid.isSelected()) {
			bill_data.get(current_bill-1).setPaid(true);
			System.out.println(bill_data.get(current_bill-1).isPaid());
			updateBill();
			confirm.setDisable(true);
			try {
				System.out.println("update clients set debt= 0 where cid = "+bill_data.get(current_bill-1).getCid()+";");
				connectDB();
				ExecuteStatement("update clients set debt = 0 where cid = "+bill_data.get(current_bill-1).getCid()+";");
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}	
		else
			try {
				double debt = bill_data.get(current_bill-1).getCash() + bill_data.get(current_bill-1).getDebt();
				System.out.println("update clients set debt = "+debt+" where cid = "+bill_data.get(current_bill-1).getCid()+";");
				connectDB();
				ExecuteStatement("update clients set debt = "+debt+" where cid = "+bill_data.get(current_bill-1).getCid()+";");
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		System.out.println(bill_data.get(current_bill-1).isPaid());
		billDetails.clear();
	}

	void updateBill() {
		try {
			System.out.println("update bill set isPaid = 1 where bill_num = "+current_bill+";");
			connectDB();
			ExecuteStatement("update bill set isPaid = 1 where bill_num = "+current_bill+";");
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void saveBill(ActionEvent event) {
		try {
			connectDB();
			String SQL = "select * from bill where bill_num="+current_bill+";";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			String SQL2 = "select product_id,product_name,product_count,price "
					+ "from order_line O, bill B "
					+ "where B.order_num = O.order_num and B.bill_num="+current_bill+";";
			Statement stmt2 = con.createStatement();
			ResultSet rs2 = stmt2.executeQuery(SQL2);
			while(rs.next()){
				PrintWriter writer = new PrintWriter(rs.getString(1)+"_"+rs.getString(5)+".txt", "UTF-8");
				writer.println(
						"Bill Number: "+rs.getString(1)+"\n"
								+"Client ID: "+rs.getString(2)+"\n"
								+"Order Number: "+rs.getString(6)+"\n");
				while(rs2.next()) {
					writer.println(
							"Product ID: "+rs2.getString(1)+"\n"
									+"Product Name: "+rs2.getString(2)+"\n"
									+"Product Count: "+rs2.getString(3)+"\n"
									+"Price Per Piece: "+rs2.getString(4)+"\n");
				}
				writer.println(
						"Cash Value: "+rs.getString(3)+"\n"
								+"Debt Value: "+rs.getString(4)+"\n"
								+"Date: "+rs.getString(5)+"\n"
								+"Paid: "+rs.getString(7)+"\n");
				System.out.println("Saved to "+rs.getString(1)+"_"+rs.getString(5)+".txt");
				Alert("Saved to "+rs.getString(1)+"_"+rs.getString(5)+".txt");
				writer.close();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	void Alert(String message) {
		javafx.scene.control.Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(message);
		alert.setTitle("Message!");
		alert.setHeaderText(null);
		alert.setResizable(false);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.show();
	}
}

