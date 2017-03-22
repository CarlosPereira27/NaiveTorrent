package br.ufla.naivetorrent.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.sqlite.SQLiteConfig;

public class DatabaseContract {

	/**
	 * URL do banco de dados.
	 */
	public static final String DATABASE_URL = "jdbc:sqlite:naiveTorrentClient.db";
	
	/**
	 * Recupera as propriedades do banco.
	 * @return propriedades do banco
	 */
	private static Properties getProperties() {
		SQLiteConfig config = new SQLiteConfig();  
        config.enforceForeignKeys(true);
        return config.toProperties();
	}
	
	/**
	 * Recupera uma conexão com o banco.
	 * @return conexão com o banco
	 * @throws SQLException
	 */
	public static Connection getConnection() 
			throws SQLException {
		return DriverManager.getConnection(DATABASE_URL, getProperties());
	}

}
