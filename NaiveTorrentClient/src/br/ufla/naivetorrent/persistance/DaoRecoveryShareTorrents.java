package br.ufla.naivetorrent.persistance;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class DaoRecoveryShareTorrents {
	
	/**
	 * Recupera a lista de shareTorrents no banco.
	 * @return lista de shareTorrents
	 */
	public List<ShareTorrent> getShareTorrents() {
		List<ShareTorrent> listTorrents = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DatabaseContract.getConnection();
			ps = connection.prepareStatement("SELECT * FROM " 
					+ MetaTorrentContract.TABLE_NAME);
			rs = ps.executeQuery();
			while (rs.next()) {
				ShareTorrent shareTorrent = new ShareTorrent();
				MetaTorrent metaTorrent = new MetaTorrent();
				Integer idTorrent = null;
				Integer idMetaInfo = null;
				MetaInfoTorrent metaInfoTorrent = null;
				shareTorrent.setMyBitfieldString(rs.getString(
						MetaTorrentContract.Columns.BITFIELD));
				shareTorrent.setMyBitfieldString(rs.getString(
						MetaTorrentContract.Columns.COMMENT));
				metaTorrent.setCreatedBy(rs.getString(
						MetaTorrentContract.Columns.CREATED_BY));
				metaTorrent.setCreationDateTime(rs.getLong(
						MetaTorrentContract.Columns.CREATION_DATE));
				shareTorrent.setDownloaded(rs.getInt(
						MetaTorrentContract.Columns.DOWNLOADED));
				metaTorrent.setEncoding(rs.getString(
						MetaTorrentContract.Columns.ENCODING));
				idTorrent = rs.getInt(
						MetaTorrentContract.Columns.ID);
				metaTorrent.setInfoHash(rs.getString(
						MetaTorrentContract.Columns.INFO_HASH));
				shareTorrent.setLastActivityTime(rs.getLong(
						MetaTorrentContract.Columns.LAST_ACTIVITY));
				shareTorrent.setSharePathString(rs.getString(
						MetaTorrentContract.Columns.SHARE_PATH));
				shareTorrent.setUploaded(rs.getInt(
						MetaTorrentContract.Columns.UPLOADED));
				metaTorrent.setTrackers(getTrackers(idTorrent));
				idMetaInfo = getIdMetaInfo(idTorrent);
				metaInfoTorrent = getMetaInfo(idMetaInfo);
				metaInfoTorrent.setMetaFiles(getMetaFiles(idMetaInfo));
				metaTorrent.setInfo(metaInfoTorrent);
				shareTorrent.setMetaTorrent(metaTorrent);
				listTorrents.add(shareTorrent);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
		return listTorrents;
	}
		
	/**
	 * Recupera a lista de trackers de um determinado torrent.
	 * @param idTorrent id do torrent
	 * @return lista de trackers
	 * @throws SQLException
	 */
	private List<Tracker> getTrackers(Integer idTorrent) 
			throws SQLException {
		List<Integer> idsTrackers = getIdsTrackers(idTorrent);
		List<Tracker> trackers = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DatabaseContract.getConnection();
			for (Integer idTracker : idsTrackers) {
				ps = connection.prepareStatement("SELECT * " 
						+ " FROM " + TrackerContract.TABLE_NAME + "\n"
						+ "WHERE " + TrackerContract.Columns.ID
						+ " = ?");
				ps.setInt(1, idTracker);
				rs = ps.executeQuery();
				if (rs.next()) {
					Tracker tracker = new Tracker();
					tracker.setIdHex(rs.getString(TrackerContract.Columns.HASH_ID));
					String ip = rs.getString(TrackerContract.Columns.HOST_NAME);
					Integer port = rs.getInt(TrackerContract.Columns.PORT);
					tracker.setSocketAddressListening(new InetSocketAddress(ip, port));
					trackers.add(tracker);
				}
			}
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
			if (connection != null)
				connection.close();
		}
		return trackers;
	}
	
	/**
	 * Recupera a lista de ids dos trackers de um determinado torrent.
	 * @param idTorrent id do torrent a ser recuperado lista de ids de trackers
	 * @return lista de ids dos trackers
	 * @throws SQLException
	 */
	public List<Integer> getIdsTrackers(Integer idTorrent) 
			throws SQLException {
		List<Integer> idsTrackers = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DatabaseContract.getConnection();
			ps = connection.prepareStatement("SELECT " 
					+ TorrentTrackerContract.Columns.ID_TRACKER 
					+ " FROM " + TorrentTrackerContract.TABLE_NAME + "\n"
					+ "WHERE " + TorrentTrackerContract.Columns.ID_TORRENT
					+ " = ?");
			ps.setInt(1, idTorrent);
			rs = ps.executeQuery();
			while (rs.next()) {
				idsTrackers.add(rs.getInt(1));
			}
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
			if (connection != null)
				connection.close();
		}
		return idsTrackers;
	}
	
	/**
	 * Recupera as informações de um determinado torrent.
	 * @param idMetaInfo id das informações
	 * @return informações de um determinado torrent
	 * @throws SQLException
	 */
	private MetaInfoTorrent getMetaInfo(Integer idMetaInfo) 
			throws SQLException {
		MetaInfoTorrent metaInfoTorrent = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DatabaseContract.getConnection();
			ps = connection.prepareStatement("SELECT *"
					+ " FROM " + MetaInfoTorrentContract.TABLE_NAME + "\n"
					+ "WHERE " + MetaInfoTorrentContract.Columns.ID
					+ " = ?");
			ps.setInt(1, idMetaInfo);
			rs = ps.executeQuery();
			if (rs.next()) {
				metaInfoTorrent = new MetaInfoTorrent();
				metaInfoTorrent.setPiecesLength(rs.getInt(
						MetaInfoTorrentContract.Columns.PIECES_LENGTH));
				metaInfoTorrent.setPiecesHashString(rs.getString(
						MetaInfoTorrentContract.Columns.PIECES_HASH));
			}
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
			if (connection != null)
				connection.close();
		}
		return metaInfoTorrent;
	}
	
	/**
	 * Recupera o id de informações de um determinado torrent.
	 * @param idTorrent id do torrent
	 * @return id das informações do torrent
	 * @throws SQLException
	 */
	public Integer getIdMetaInfo(Integer idTorrent) 
			throws SQLException {
		Integer idMetaFile = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DatabaseContract.getConnection();
			ps = connection.prepareStatement("SELECT " 
					+ TorrentInfoContract.Columns.ID_INFO
					+ " FROM " + TorrentInfoContract.TABLE_NAME + "\n"
					+ "WHERE " + TorrentInfoContract.Columns.ID_TORRENT
					+ " = ?");
			ps.setInt(1, idTorrent);
			rs = ps.executeQuery();
			if (rs.next()) {
				idMetaFile = rs.getInt(1);
			}
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
			if (connection != null)
				connection.close();
		}
		return idMetaFile;
	}
	
	/**
	 * Recupera a lista de arquivos de um determinado torrent.
	 * @param idInfo id da informação torrent
	 * @return lista de arquivos
	 * @throws SQLException
	 */
	private List<MetaFileTorrent> getMetaFiles(Integer idInfo) 
			throws SQLException {
		List<Integer> idsMetaFiles = getIdsMetaFiles(idInfo);
		List<MetaFileTorrent> metaFiles = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DatabaseContract.getConnection();
			for (Integer idMetaFile : idsMetaFiles) {
				ps = connection.prepareStatement("SELECT * " 
						+ " FROM " + MetaFileTorrentContract.TABLE_NAME + "\n"
						+ "WHERE " + MetaFileTorrentContract.Columns.ID
						+ " = ?");
				ps.setInt(1, idMetaFile);
				rs = ps.executeQuery();
				if (rs.next()) {
					MetaFileTorrent metaFile = new MetaFileTorrent();
					metaFile.setLength(rs.getLong(
							MetaFileTorrentContract.Columns.LENGTH));
					metaFile.setMd5sumHex(rs.getString(
							MetaFileTorrentContract.Columns.MD5SUM));
					metaFile.setPathFile(rs.getString(
							MetaFileTorrentContract.Columns.PATH_FILE));
					metaFiles.add(metaFile);
				}
			}
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
			if (connection != null)
				connection.close();
		}
		return metaFiles;
	}
	
	/**
	 * Recupera a lista de ids de arquivos referentes à informação de um determinado torrent.
	 * @param idInfo id da informação do torrent
	 * @return lista de ids dos arquivos
	 * @throws SQLException
	 */
	public List<Integer> getIdsMetaFiles(Integer idInfo) 
			throws SQLException {
		List<Integer> idsMetaFiles = new ArrayList<>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DatabaseContract.getConnection();
			ps = connection.prepareStatement("SELECT " 
					+ InfoFileContract.Columns.ID_FILE
					+ " FROM " + InfoFileContract.TABLE_NAME + "\n"
					+ "WHERE " + InfoFileContract.Columns.ID_INFO
					+ " = ?");
			ps.setInt(1, idInfo);
			rs = ps.executeQuery();
			while (rs.next()) {
				idsMetaFiles.add(rs.getInt(1));
			}
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
			if (connection != null)
				connection.close();
		}
		return idsMetaFiles;
	}
	
	/**
	 * Recupera o id de um determinado torrent
	 * @param con conexão com o banco
	 * @param infoHash info do hash do torrent a ser recuperado
	 * @return id do torrent
	 * @throws SQLException
	 */
	public Integer getIdTorrent(String infoHash) 
			throws SQLException {
		Integer idTorrent = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DatabaseContract.getConnection();
			ps = con.prepareStatement("SELECT "
					+ MetaTorrentContract.Columns.ID
					+ " FROM " + MetaTorrentContract.TABLE_NAME + "\n"
					+ "WHERE " + MetaTorrentContract.Columns.INFO_HASH
					+ " = ?");
			ps.setString(1, infoHash);
			rs = ps.executeQuery();
			if (rs.next()) {
				idTorrent = rs.getInt(1);
			}
		} finally {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
		}
		return idTorrent;
	}

}
