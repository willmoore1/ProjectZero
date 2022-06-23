package src.com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil { // singleton connection class
	
	private static Connection conn = null;
	private ConnectionUtil() {}
	
	public static Connection getConnection() {
		try {
			if(conn != null && !conn.isClosed()) {
				return conn;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return(null);
		}
		String url = System.getenv("DB_URL");
		String username = System.getenv("DB_USERNAME");
		String password = System.getenv("DB_PASSWORD");
		try {
			conn = DriverManager.getConnection(url, username, password);
			//System.out.println("Established Connection to database");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//System.out.println("Cannot establish connection");
			e.printStackTrace();
		}
		
		return conn;
		
		
	}

}
