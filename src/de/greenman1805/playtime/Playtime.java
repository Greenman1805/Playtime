package de.greenman1805.playtime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.TreeMap;
import java.util.UUID;

public class Playtime {

	private static boolean hasEntry(UUID uuid) {
		MySQL.checkConnection();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT playtime FROM playtime WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static int getPlaytime(UUID uuid) {
		MySQL.checkConnection();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT playtime FROM playtime WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt("playtime");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private static void createEntry(UUID uuid) {
		MySQL.checkConnection();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO playtime (UUID, playtime) VALUES (?,?)");
			ps.setString(1, uuid.toString());
			ps.setInt(2, 0);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addPlaytime(UUID uuid, int toAdd) {
		MySQL.checkConnection();
		if (!hasEntry(uuid)) {
			createEntry(uuid);
		}
		try {
			int current = getPlaytime(uuid);

			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE playtime SET playtime = ? WHERE UUID = ?");
			ps.setInt(1, current + toAdd);
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static TreeMap<Integer, UUID> getTopPlaytimes() {
		MySQL.checkConnection();
		String sql = "SELECT * FROM playtime ORDER BY playtime DESC LIMIT 10;";
		TreeMap<Integer, UUID> top = new TreeMap<Integer, UUID>(Collections.reverseOrder());

		try {
			Statement statement = MySQL.getConnection().createStatement();
			ResultSet resultset = statement.executeQuery(sql);
			while (resultset.next()) {

				UUID uuid = UUID.fromString(resultset.getString("UUID"));
				int time = resultset.getInt("playtime");
				top.put(time, uuid);
			}

			resultset.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return top;
	}

}
