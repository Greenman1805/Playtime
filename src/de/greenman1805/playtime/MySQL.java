package de.greenman1805.playtime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL {
	public static String host;
	public static String port;
	public static String user;
	public static String password;
	public static String database;
	private static Connection con;

	public static void refreshConnection() {
		MySQL.checkConnection();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT version()");
			ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static boolean openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
			return true;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	public static void checkConnection() {
		try {
			if (con.isClosed() || con == null) {
				MySQL.openConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con = null;
	}

	public static Connection getConnection() {
		return con;
	}

}