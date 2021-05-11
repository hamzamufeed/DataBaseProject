package application;

import java.io.IOException;
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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReportController implements Initializable{
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
    private Button logOut;
	
	@FXML
	private DatePicker fromDate;

	@FXML
	private DatePicker toDate;

	@FXML
	private TextField text1;

	@FXML
	private TextField text2;
	
    @FXML
    private TextField text3;

    @FXML
    private TextField text4;

	@FXML
	private Button confirm;

	@FXML
	private Button report;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		text1.clear();
		text2.clear();
		fromDate.getEditor().clear();
		toDate.getEditor().clear();
	}

	@FXML
	void confirmDate(ActionEvent event) {
		if(fromDate.getValue() == null || toDate.getValue() == null) {
			Alert("Please Specify a Date");
		}
		else {
			int sales_count = 0;
			double sales_value = 0;
			int goods_count = 0;
			double goods_value = 0;
			try {
				connectDB();
				String SQL = "select product_count from orders O, order_line OL, bill B where order_date > '"+Date.valueOf(fromDate.getValue())+
						"'and order_date < '"+Date.valueOf(toDate.getValue())+"' and O.order_num = OL.order_num = B.order_num and B.isPaid =1;";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(SQL);
				while ( rs.next()) {
					sales_count += Integer.parseInt(rs.getString(1));
				}
				text1.setText(String.valueOf(sales_count));
				
				SQL = "select total_price from bill where bill_date > '"+Date.valueOf(fromDate.getValue())+
						"'and bill_date < '"+Date.valueOf(toDate.getValue())+"' and isPaid = 1;";
				rs = stmt.executeQuery(SQL);
				while ( rs.next()) {
					sales_value += Double.parseDouble(rs.getString(1));
				}
				text2.setText(String.valueOf(sales_value));
				
				SQL = "select goods_count from goods;";
				rs = stmt.executeQuery(SQL);
				while ( rs.next()) {
					goods_count += Integer.parseInt(rs.getString(1));
				}
				text3.setText(String.valueOf(goods_count));
				
				SQL = "select goods_count, wholesale_price from goods;";
				rs = stmt.executeQuery(SQL);
				while ( rs.next()) {
					goods_value = goods_value + (Integer.parseInt(rs.getString(1)) * Double.parseDouble(rs.getString(2)));
				}
				text4.setText(String.valueOf(goods_value));
			}catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
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

