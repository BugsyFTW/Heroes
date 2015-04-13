package me.bugsyftw.heroes.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

public class MySQL extends Database {
	
	private final String user;
	private final String database;
	private final String password;
	private final String port;
	private final String hostname;

	private Connection connection;

	public MySQL(Plugin plugin, String hostname, String port, String database, String username, String password) {
		super(plugin);
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.user = username;
		this.password = password;
		this.connection = null;
	}

	@Override
	public Connection openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password);
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Could not connect to MySQL server! because: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			plugin.getLogger().log(Level.SEVERE, "JDBC Driver not found!");
		}
		return connection;
	}

	@Override
	public boolean checkConnection() {
		return connection != null;
	}

	@Override
	public Connection getConnection() {
		return connection;
	}

	@Override
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				plugin.getLogger().log(Level.SEVERE, "Error closing the MySQL Connection!");
				e.printStackTrace();
			}
		}
	}
}