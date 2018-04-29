package airbnb.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DBManager {

	INSTANCE;

	private static final String DB_IP = "localhost";
	private static final String DB_PORT = "3306";
	private static final String DB_NAME = "airbnb";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "";
	private static final String URL = "jdbc:mysql://" + DB_IP + ":" + DB_PORT + "/" + DB_NAME;

	private Connection conn;

	private DBManager() {
		// load driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry, Driver not loaded or does not exist! Aborting.");
			return;
		}
		System.out.println("Driver loaded");

		// create connection
		try {
			conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);

		} catch (SQLException e) {
			System.out.println("Sorry, connection failed. Wrong credentials or url to DB");
			System.out.println(e.getMessage());
		}
	}

	public Connection getConnection() {
		return this.conn;
	}

	public void closeConnection() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}