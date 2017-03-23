package br.ufla.naivetorrent.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import br.ufla.naivetorrent.domain.file.MetaTorrent;
import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.persistance.contract.MetaFileTorrentContract;
import br.ufla.naivetorrent.persistance.contract.MetaInfoTorrentContract;
import br.ufla.naivetorrent.persistance.contract.MetaTorrentContract;

public class DaoRemoveShareTorrent {

	/**
	 * Remove um torrent do banco.
	 * 
	 * @param shareTorrent
	 *            torrent a ser removido do banco
	 * @return true se torrent foi removido com sucesso, caso contrário false.
	 * @throws SQLException
	 */
	public boolean remove(ShareTorrent shareTorrent) 
			throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			DaoRecoveryShareTorrents daoRecoveryShareTorrents = new DaoRecoveryShareTorrents();
			MetaTorrent metaTorrent = shareTorrent.getMetaTorrent();
			Integer idTorrent = daoRecoveryShareTorrents.getIdTorrent(metaTorrent.getInfoHashHex());
			Integer idInfo = daoRecoveryShareTorrents.getIdMetaInfo(idTorrent);
			List<Integer> idsMetaFiles = daoRecoveryShareTorrents.getIdsMetaFiles(idInfo);
			connection = DatabaseContract.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(
					"DELETE FROM " + MetaTorrentContract.TABLE_NAME 
					+ "\nWHERE " + MetaTorrentContract.Columns.ID + " = ?");
			ps.setInt(1, idTorrent);
			if (!ps.execute()) {
				connection.rollback();
				return false;
			}
			if (ps != null) {
				ps.close();
			}
			ps = connection.prepareStatement(
					"DELETE FROM " + MetaInfoTorrentContract.TABLE_NAME 
					+ "\nWHERE " + MetaInfoTorrentContract.Columns.ID + " = ?");
			ps.setInt(1, idInfo);
			if (!ps.execute()) {
				connection.rollback();
				return false;
			}
			for (Integer idMetaFile : idsMetaFiles) {
				ps = connection.prepareStatement(
						"DELETE FROM " + MetaFileTorrentContract.TABLE_NAME 
						+ "\nWHERE " + MetaFileTorrentContract.Columns.ID + " = ?");
				ps.setInt(1, idMetaFile);
				if (!ps.execute()) {
					connection.rollback();
					return false;
				}
				if (ps != null) {
					ps.close();
				}
			}
			connection.commit();
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			connection.rollback();
			return false;
		} finally {
			connection.setAutoCommit(true);
			try {
				if (ps != null) {
					ps.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
	}

}
