package br.ufla.naivetorrent.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufla.naivetorrent.domain.file.MetaFileTorrent;
import br.ufla.naivetorrent.domain.file.MetaInfoTorrent;
import br.ufla.naivetorrent.domain.file.MetaTorrent;
import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.domain.tracker.Tracker;
import br.ufla.naivetorrent.persistance.contract.InfoFileContract;
import br.ufla.naivetorrent.persistance.contract.MetaFileTorrentContract;
import br.ufla.naivetorrent.persistance.contract.MetaInfoTorrentContract;
import br.ufla.naivetorrent.persistance.contract.MetaTorrentContract;
import br.ufla.naivetorrent.persistance.contract.TorrentInfoContract;
import br.ufla.naivetorrent.persistance.contract.TorrentTrackerContract;
import br.ufla.naivetorrent.persistance.contract.TrackerContract;

public class DaoInsertShareTorrent {

	/**
	 * Insere um ShareTorrent no banco.
	 * 
	 * @param shareTorrent
	 * @throws SQLException
	 */
	public void insertShareTorrent(ShareTorrent shareTorrent) throws SQLException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DatabaseContract.PREFIX_SQLITE 
					+ DatabaseContract.DATABASE_NAME);
			connection.setAutoCommit(false);
			MetaTorrent metaTorrent = shareTorrent.getMetaTorrent();
			MetaInfoTorrent metaInfo = metaTorrent.getInfo();
			Integer idMetaTorrent = insertMetaTorrent(connection, shareTorrent);
			List<Integer> idsTrackers = insertTrackers(connection, 
					metaTorrent.getTrackers());
			Integer idMetaInfo = insertInfoTorrent(connection, metaInfo);
			List<Integer> idsMetaFiles = insertMetaFilesTorrent(connection, 
					metaInfo.getMetaFiles());
			insertTorrentTrackers(connection, idMetaTorrent, idsTrackers);
			insertTorrentInfo(connection, idMetaTorrent, idMetaInfo);
			insertInfoFiles(connection, idMetaInfo, idsMetaFiles);
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
	 * Insere um MetaTorrent no banco.
	 * 
	 * @param con
	 *            conexão com o banco
	 * @param shareTorrent
	 *            dados do torrent a ser inserido
	 * @return id do MetaTorrent inserido
	 * @throws SQLException
	 */
	private Integer insertMetaTorrent(Connection con, ShareTorrent shareTorrent) 
			throws SQLException {
		Integer idMetaTorrent = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("INSERT INTO " + MetaTorrentContract.TABLE_NAME + " ("
					+ MetaTorrentContract.Columns.BITFIELD + ", "
					+ MetaTorrentContract.Columns.COMMENT + ", "
					+ MetaTorrentContract.Columns.CREATED_BY + ", " 
					+ MetaTorrentContract.Columns.CREATION_DATE + ", "
					+ MetaTorrentContract.Columns.DOWNLOADED + ", " 
					+ MetaTorrentContract.Columns.ENCODING + ", "
					+ MetaTorrentContract.Columns.INFO_HASH + ", " 
					+ MetaTorrentContract.Columns.LAST_ACTIVITY + ", "
					+ MetaTorrentContract.Columns.SHARE_PATH + ", " 
					+ MetaTorrentContract.Columns.UPLOADED + ")\n"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", 
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, shareTorrent.getMyBitfieldString());
			MetaTorrent metaTorrent = shareTorrent.getMetaTorrent();
			ps.setString(2, metaTorrent.getComment());
			ps.setString(3, metaTorrent.getCreatedBy());
			ps.setLong(4, metaTorrent.getCreationDateTime());
			ps.setInt(5, shareTorrent.getDownloaded());
			ps.setString(6, metaTorrent.getEncoding());
			ps.setString(7, metaTorrent.getInfoHashHex());
			ps.setLong(8, shareTorrent.getLastActivityTime());
			ps.setString(9, shareTorrent.getSharePathString());
			ps.setInt(10, shareTorrent.getUploaded());
			ps.execute();
			rs = ps.getGeneratedKeys();
			if (rs.next())
				idMetaTorrent = rs.getInt(1);
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
		}
		return idMetaTorrent;
	}

	/**
	 * Insere um InfoTorrent no banco.
	 * 
	 * @param con
	 *            conexão com o banco
	 * @param metaInfoTorrent
	 *            dados do torrent a ser inserido
	 * @return id do InfoTorrent inserido
	 * @throws SQLException
	 */
	private Integer insertInfoTorrent(Connection con, 
			MetaInfoTorrent metaInfoTorrent) 
					throws SQLException {
		Integer idInfoTorrent = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(
					"INSERT INTO " + MetaInfoTorrentContract.TABLE_NAME + " ("
							+ MetaInfoTorrentContract.Columns.PIECES_HASH + ", "
							+ MetaInfoTorrentContract.Columns.PIECES_LENGTH + ")\n" 
							+ "VALUES (?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, metaInfoTorrent.getPiecesHashString());
			ps.setInt(2, metaInfoTorrent.getPiecesLength());
			rs = ps.getGeneratedKeys();
			if (rs.next())
				idInfoTorrent = rs.getInt(1);
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
		}
		return idInfoTorrent;
	}

	/**
	 * Insere uma lista de MetaFileTorrent no banco.
	 * 
	 * @param con
	 *            conexão com o banco
	 * @param metaFiles
	 *            lista de MetaFileTorrent que serão inseridos
	 * @return lista de ids dos MetaFileTorrent inseridos.
	 * @throws SQLException
	 */
	private List<Integer> insertMetaFilesTorrent(Connection con, 
			List<MetaFileTorrent> metaFiles) 
					throws SQLException {
		List<Integer> idMetaFiles = new ArrayList<>();
		for (MetaFileTorrent metaFile : metaFiles) {
			idMetaFiles.add(insertMetaFileTorrent(con, metaFile));
		}
		return idMetaFiles;
	}

	/**
	 * Insere um MetaFileTorrent no banco.
	 * 
	 * @param con
	 *            conexão com o banco
	 * @param metaFile
	 *            MetaFileTorrent a ser inserido
	 * @return id do MetaFileTorrent inserido.
	 * @throws SQLException
	 */
	private Integer insertMetaFileTorrent(Connection con, 
			MetaFileTorrent metaFile) 
					throws SQLException {
		Integer idMetaFile = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(
					"INSERT INTO " + MetaFileTorrentContract.TABLE_NAME + " (" 
					+ MetaFileTorrentContract.Columns.LENGTH + ", " 
					+ MetaFileTorrentContract.Columns.MD5SUM + ", "
					+ MetaFileTorrentContract.Columns.PATH_FILE + ")\n" 
					+ "VALUES (?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, metaFile.getLength());
			ps.setString(2, metaFile.getMd5sumHex());
			ps.setString(3, metaFile.getPathFile());
			ps.execute();
			rs = ps.getGeneratedKeys();
			if (rs.next())
				idMetaFile = rs.getInt(1);
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
		}
		return idMetaFile;
	}

	/**
	 * Insere uma lista de trackers no banco.
	 * 
	 * @param con
	 *            conexão com o banco
	 * @param trackers
	 *            lista de trackers que serão inseridos
	 * @return lista de ids dos trackers
	 * @throws SQLException
	 */
	private List<Integer> insertTrackers(Connection con, 
			List<Tracker> trackers) 
					throws SQLException {
		List<Integer> idTrackers = new ArrayList<>();
		for (Tracker tracker : trackers) {
			Integer idTracker = getTrackerId(con, tracker);
			if (idTracker == null) {
				idTracker = insertTracker(con, tracker);
			}
			idTrackers.add(idTracker);
		}
		return idTrackers;
	}

	/**
	 * Insere um tracker no banco.
	 * 
	 * @param con
	 *            conexão com o banco
	 * @param tracker
	 *            tracker que deseja inserir
	 * @return id do tracker inserido
	 * @throws SQLException
	 */
	private Integer insertTracker(Connection con, Tracker tracker) 
			throws SQLException {
		Integer idTracker = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("INSERT INTO " + TrackerContract.TABLE_NAME + " ("
					+ TrackerContract.Columns.HASH_ID + ", " 
					+ TrackerContract.Columns.HOST_NAME + ", "
					+ TrackerContract.Columns.PORT + ")\n" + 
					"VALUES (?, ?, ?);", 
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, tracker.getIdHex());
			ps.setString(2, tracker.getIpOrHostName());
			ps.setInt(3, tracker.getPort());
			ps.execute();
			rs = ps.getGeneratedKeys();
			if (rs.next())
				idTracker = rs.getInt(1);
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
		}
		return idTracker;
	}

	/**
	 * Recupera o id de um determinado tracker.
	 * 
	 * @param con
	 *            conexão com o banco
	 * @param tracker
	 *            tracker que deseja recuperar id
	 * @return id do tracker
	 * @throws SQLException
	 */
	private Integer getTrackerId(Connection con, Tracker tracker) throws SQLException {
		Integer idTracker = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("SELECT " + TrackerContract.Columns.ID 
					+ " FROM " + TrackerContract.TABLE_NAME + "\n" 
					+ "WHERE " + TrackerContract.Columns.HASH_ID + " = ?");
			ps.setString(1, tracker.getIdHex());
			rs = ps.executeQuery();
			if (rs.next())
				idTracker = rs.getInt(1);
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
		}
		return idTracker;
	}

	/**
	 * Insere um TorrentTracker no banco.
	 * 
	 * @param con
	 *            conexão com o banco
	 * @param idTorrent
	 *            id do torrent
	 * @param idTracker
	 *            id do tracker
	 * @throws SQLException
	 */
	private void insertTorrentTracker(Connection con, Integer idTorrent, 
			Integer idTracker) 
					throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("INSERT INTO " + TorrentTrackerContract.TABLE_NAME + " ("
					+ TorrentTrackerContract.Columns.ID_TORRENT + ", " 
					+ TorrentTrackerContract.Columns.ID_TRACKER + ")\n" 
					+ "VALUES (?, ?);");
			ps.setInt(1, idTorrent);
			ps.setInt(2, idTracker);
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
		}
	}

	/**
	 * Insere uma lista de TorrentTrackers no banco.
	 * 
	 * @param con
	 *            conexão com o banco
	 * @param idTorrent
	 *            id do torrent
	 * @param idsTrackers
	 *            lista de ids do trackers
	 * @throws SQLException
	 */
	private void insertTorrentTrackers(Connection con, Integer idTorrent, 
			List<Integer> idsTrackers)
					throws SQLException {
		for (Integer idTracker : idsTrackers) {
			insertTorrentTracker(con, idTorrent, idTracker);
		}
	}

	/**
	 * Insere um TorrentInfo no banco.
	 * 
	 * @param con
	 *            conexão com o banco
	 * @param idTorrent
	 *            id do torrent
	 * @param idInfo
	 *            id da info do torrent
	 * @throws SQLException
	 */
	private void insertTorrentInfo(Connection con, Integer idTorrent, 
			Integer idInfo) 
					throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(
					"INSERT INTO " + TorrentInfoContract.TABLE_NAME + " (" 
					+ TorrentInfoContract.Columns.ID_TORRENT + ", " 
					+ TorrentInfoContract.Columns.ID_INFO + ")\n" 
					+ "VALUES (?, ?);");
			ps.setInt(1, idTorrent);
			ps.setInt(2, idInfo);
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
		}
	}

	/**
	 * Insere um InfoFile no banco.
	 * 
	 * @param con
	 *            conexão com o banco
	 * @param idInfo
	 *            id da info
	 * @param idFile
	 *            id do arquivo
	 * @throws SQLException
	 */
	private void insertInfoFile(Connection con, Integer idInfo, 
			Integer idFile) 
					throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(
					"INSERT INTO " + InfoFileContract.TABLE_NAME + " (" 
					+ InfoFileContract.Columns.ID_INFO + ", "
					+ InfoFileContract.Columns.ID_FILE + ")\n" 
					+ "VALUES (?, ?);");
			ps.setInt(1, idInfo);
			ps.setInt(2, idFile);
			ps.execute();
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
		}
	}

	/**
	 * Insere uma lista de InfoFile no banco.
	 * 
	 * @param con
	 *            conexão com o banco
	 * @param idInfo
	 *            id da info
	 * @param idsFiles
	 *            lista de ids dos arquivos
	 * @throws SQLException
	 */
	private void insertInfoFiles(Connection con, Integer idInfo, 
			List<Integer> idsFiles) 
					throws SQLException {
		for (Integer idFile : idsFiles) {
			insertInfoFile(con, idInfo, idFile);
		}
	}

}
