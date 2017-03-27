package br.ufla.naivetorrent.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.ufla.naivetorrent.domain.file.MetaTorrent;
import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.persistance.contract.MetaTorrentContract;

public class DaoUpdateShareTorrent {

	/**
	 * Realiza a atualização de um shareTorrent.
	 * 
	 * @param shareTorrent
	 *            share torrent a ser atualizado
	 * @return true se shareTorrent foi atualizado com sucesso, false caso
	 *         contrário
	 * @throws SQLException
	 */
	public boolean update(ShareTorrent shareTorrent) 
			throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = DatabaseContract.getConnection();
			DaoRecoveryShareTorrents daoRecoveryShareTorrents = new DaoRecoveryShareTorrents();
			MetaTorrent metaTorrent = shareTorrent.getMetaTorrent();
			Integer idTorrent = daoRecoveryShareTorrents.getIdTorrent(metaTorrent.getInfoHashHex());
			ps = connection.prepareStatement(
					"UPDATE " + MetaTorrentContract.TABLE_NAME 
					+ "\nSET " + MetaTorrentContract.Columns.BITFIELD + " = ?, " 
					+ MetaTorrentContract.Columns.DOWNLOADED + " = ?, " 
					+ MetaTorrentContract.Columns.LEFT + " = ?, "
					+ MetaTorrentContract.Columns.LAST_ACTIVITY + " = ?, "
					+ MetaTorrentContract.Columns.SHARE_PATH + " = ?, " 
					+ MetaTorrentContract.Columns.UPLOADED + " = ?\n" 
					+ "WHERE " + MetaTorrentContract.Columns.ID + " = ?");
			ps.setString(1, shareTorrent.getMyBitfieldString());
			ps.setLong(2, shareTorrent.getDownloaded());
			ps.setLong(3, shareTorrent.getLeft());
			ps.setLong(4, shareTorrent.getLastActivityTime());
			ps.setString(5, shareTorrent.getSharePathString());
			ps.setLong(6, shareTorrent.getUploaded());
			ps.setInt(7, idTorrent);
			return ps.execute();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		} finally {
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
