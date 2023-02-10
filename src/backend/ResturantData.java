package backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ResturantData 
{
	static Database database = Database.getInstance();
	static ArrayList<Orders> order = new ArrayList<Orders>();
	static ArrayList<Orders> searchData = new ArrayList<Orders>();
	static ArrayList<Tables> tables = new ArrayList<Tables>();
	Statement st = null;
	static ResultSet rs = null;
	
	public static void getOrdersData() {
		rs = database.executeQuery("SELECT * from orders;");
		try {
			while(rs.next()) {
				order.add((new Orders(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getString(4),rs.getTimestamp(5),rs.getBoolean(6))));
			}
			
		} catch (SQLException e) {
			e.getStackTrace();
		}
	}
	public static void getTablesData() {
		rs = database.executeQuery("SELECT * from TableAssignment;");
		try {
			while (rs.next()) {
				tables.add(new Tables(rs.getInt(1),rs.getInt(2),rs.getString(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void getSearchData(String searchItem)
	{
		rs = database.executeQuery("SELECT * FROM orders where STAFF_ID   = '2';"); 
		
		try {
			while(rs.next()) {
				searchData.add((new Orders(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getString(4),rs.getTimestamp(5),rs.getBoolean(6))));
				System.out.println((rs.getInt(1) + rs.getInt(2) + rs.getInt(3) + rs.getString(4) + rs.getTimestamp(5) + rs.getBoolean(6) + "\n"));
				System.out.println(searchData.size());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static ArrayList<Orders> getOrdersList() {
		getOrdersData();
		return order;
	}
	public static ArrayList<Tables> getTablesList(){
		getTablesData();
		return tables;
	}
	public static ArrayList<Orders>getSearhDataList(String searchItem){
		getSearchData(searchItem);
		return searchData;
		
	}
	


	
}
