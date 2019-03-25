package de.greenman1805.playtime;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeMain extends Plugin {
	public static String datafolder = "plugins//Playtime";

	@Override
	public void onEnable() {
		setupConfig();
		getValues();

		if (!MySQL.openConnection()) {
			System.out.println("MySQL Verbindung konnte nicht hergestellt werden!");
			return;
		}
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playtime (UUID VARCHAR(100), playtime INT);");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new PlaytimeCommand());

		ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {

			@Override
			public void run() {
				for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
					Playtime.addPlaytime(p.getUniqueId(), 1);
				}
			}

		}, 1, 1, TimeUnit.MINUTES);

		ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {

			@Override
			public void run() {
				MySQL.refreshConnection();
			}

		}, 1, 1, TimeUnit.HOURS);
	}

	@Override
	public void onDisable() {
		MySQL.closeConnection();
	}

	private void setupConfig() {
		File file1 = new File(datafolder);
		File file2 = new File(datafolder, "config.yml");

		if (!file1.isDirectory()) {
			file1.mkdir();
		}

		if (!file2.exists()) {
			try {
				file2.createNewFile();
				Configuration data = null;
				try {
					data = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file2);
				} catch (IOException e) {
					e.printStackTrace();
				}
				data.set("MySQL.host", "localhost");
				data.set("MySQL.port", "3306");
				data.set("MySQL.user", "user");
				data.set("MySQL.password", "user");
				data.set("MySQL.database", "database");
				try {
					ConfigurationProvider.getProvider(YamlConfiguration.class).save(data, file2);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void getValues() {
		File file = new File(datafolder, "config.yml");
		Configuration data = null;
		try {
			data = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MySQL.host = data.getString("MySQL.host");
		MySQL.port = data.getString("MySQL.port");
		MySQL.user = data.getString("MySQL.user");
		MySQL.password = data.getString("MySQL.password");
		MySQL.database = data.getString("MySQL.database");
	}

}
