package br.ufla.naivetorrent.domain.file;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Date;
import java.util.Map;

import br.ufla.naivetorrent.domain.peer.Peer;
import br.ufla.naivetorrent.util.UtilByteString;

public class ShareTorrent {
	
	private Peer me;
	private Map<ByteBuffer, Peer> idToPeers;
	private Map<ByteBuffer, Peer> idToPeersConnected;
	private Map<Peer, BitSet> idToBitfield;
	private Integer uploaded;
	private Integer downloaded;
	private MetaTorrent metaTorrent;
	private BitSet myBitfield;
	private Date lastActivity;
	private File sharePath;


	/**
	 * Recupera o bitfield em forma de string.
	 * @return bitfield em forma de string
	 */
	public String getMyBitfieldString() {
		return UtilByteString.bytesToString(ByteBuffer.wrap(
				myBitfield.toByteArray()));
	}
	
	public void setMyBitfieldString(String myBitfieldString) {
		if (myBitfieldString != null) {
			
		}
	}
	
	/**
	 * Recupera o tempo da última atividade do torrent
	 * @return tempo da última atividade do torrent, se não foi definido null
	 */
	public Long getLastActivityTime() {
		if (lastActivity == null)
			return null;
		return lastActivity.getTime();
	}
	
	/**
	 * Define a data da última atividade em relação ao tempo da data.
	 * @param lastActivityTime tempo da última atividade
	 */
	public void setLastActivityTime(Long lastActivityTime) {
		if (lastActivityTime != null)
			lastActivity = new Date(lastActivityTime);
	}
	
	/**
	 * Recupera o caminho de compartilhamento do torrent em uma string
	 * @return string com o caminho de compartilhamento do torrent
	 */
	public String getSharePathString() {
		return sharePath.getPath();
	}
	
	/**
	 * Define sharePath através da string que contém o caminho.
	 * @param sharePathString tring que contém o caminho
	 */
	public void setSharePathString(String sharePathString) {
		if (sharePathString != null)
			sharePath = new File(sharePathString);
	}
	
	
	// MÉTODOS ACESSORES
	public Peer getMe() {
		return me;
	}
	public Map<ByteBuffer, Peer> getIdToPeers() {
		return idToPeers;
	}
	public Map<ByteBuffer, Peer> getIdToPeersConnected() {
		return idToPeersConnected;
	}
	public Map<Peer, BitSet> getIdToBitfield() {
		return idToBitfield;
	}
	public Integer getUploaded() {
		return uploaded;
	}
	public Integer getDownloaded() {
		return downloaded;
	}
	public MetaTorrent getMetaTorrent() {
		return metaTorrent;
	}
	public BitSet getMyBitfield() {
		return myBitfield;
	}
	public File getSharePath() {
		return sharePath;
	}
	public Date getLastActivity() {
		return lastActivity;
	}
	
	
	

}
