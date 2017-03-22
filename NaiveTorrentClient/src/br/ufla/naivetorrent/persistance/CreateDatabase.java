package br.ufla.naivetorrent.persistance;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufla.naivetorrent.persistance.contract.InfoFileContract;
import br.ufla.naivetorrent.persistance.contract.MetaFileTorrentContract;
import br.ufla.naivetorrent.persistance.contract.MetaInfoTorrentContract;
import br.ufla.naivetorrent.persistance.contract.MetaTorrentContract;
import br.ufla.naivetorrent.persistance.contract.TorrentInfoContract;
import br.ufla.naivetorrent.persistance.contract.TorrentTrackerContract;
import br.ufla.naivetorrent.persistance.contract.TrackerContract;

public class CreateDatabase {

	/**
	 * Recupera a lista de sql para criar as tabelas do banco.
	 * 
	 * @return lista de sql para criar as tabelas do banco
	 */
	private List<String> getSqlCreateTables() {
		List<String> sqlCreateTables = new ArrayList<>();
		sqlCreateTables.add(MetaTorrentContract.CREATE_TABLE);
		sqlCreateTables.add(MetaInfoTorrentContract.CREATE_TABLE);
		sqlCreateTables.add(MetaFileTorrentContract.CREATE_TABLE);
		sqlCreateTables.add(TrackerContract.CREATE_TABLE);
		sqlCreateTables.add(TorrentTrackerContract.CREATE_TABLE);
		sqlCreateTables.add(TorrentInfoContract.CREATE_TABLE);
		sqlCreateTables.add(InfoFileContract.CREATE_TABLE);
		return sqlCreateTables;
	}

	/**
	 * Cria o banco, caso já exista não faz nada.
	 * @throws SQLException 
	 */
	public void create() throws SQLException {
		List<String> sqlCreateTables = getSqlCreateTables();
		Connection connection = null;
		try {
			connection = DatabaseContract.getConnection();
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			for (String sql : sqlCreateTables) {
				statement.execute(sql);
			}
			connection.commit();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			connection.rollback();
		} finally {
			connection.setAutoCommit(true);
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
	}
	
	/**
	 * Teste
	 */
	public static void main(String[] args) {
		try {
			new CreateDatabase().create();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
