package br.ufla.naivetorrent.domain.file;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import br.ufla.naivetorrent.connection.ManagerConnections;
import br.ufla.naivetorrent.domain.peer.Peer;
import br.ufla.naivetorrent.peer.protocol.DownloadStrategy;
import br.ufla.naivetorrent.util.UtilByteString;

public class ShareTorrent {
	
	public static final int MAX_DOWNLOADING = 8;
	
	class SimpleManagerConnection implements Runnable {

		@Override
		public void run() {
			while (true) {
			}
			
		}
		
	}
	
	@SuppressWarnings("unused")
	private ManagerConnections managerConnections;
	private Peer me;
	private DownloadStrategy downloadStrategy;
	private List<Integer> nextDownloadPieces;
	private List<Integer> onDonwloading;
	private ConcurrentMap<MetaFileTorrent, Boolean> fileIsCompleted;
	private ConcurrentMap<MetaFileTorrent, FileLimits> fileToLimits;
	private ConcurrentMap<ByteBuffer, Peer> idToPeersUndefined;
	private ConcurrentMap<ByteBuffer, Peer> idToPeers;
	private ConcurrentMap<ByteBuffer, Peer> idToPeersConnected;
	private ConcurrentMap<Peer, BitSet> peerToBitfield;
	private AtomicInteger uploaded;
	private AtomicInteger downloaded;
	private MetaTorrent metaTorrent;
	private BitSet myBitfield;
	private Date lastActivity;
	private File sharePath;
	private boolean pause;
	private boolean seeder;
	
	public void getMoreRarestPieces() {
		synchronized (nextDownloadPieces) {
			nextDownloadPieces.addAll(downloadStrategy.getPieces());
		}
	}
	
	
	public ShareTorrent() {
		fileToLimits = new ConcurrentHashMap<>();
		List<MetaFileTorrent> metaFiles = new ArrayList<>();
		long limit = 0;
		for (MetaFileTorrent metaFile : metaFiles) {
			fileToLimits.put(metaFile, 
					new FileLimits(limit, limit + metaFile.getLength()));
			limit += metaFile.getLength();
		}
	}
	
	public void disconectedPeer(Peer peer) {
		synchronized (idToPeersUndefined) {
			idToPeersUndefined.remove(peer.getId());
		}
		synchronized (idToPeers) {
			idToPeers.remove(peer.getId());
		}
		synchronized (idToPeersConnected) {
			idToPeersConnected.remove(peer.getId());
		}
		synchronized (peerToBitfield) {
			peerToBitfield.remove(peer);
		}
	}
	
	public void definiedPeer(Peer peer) {
		synchronized (idToPeersUndefined) {
			idToPeersUndefined.remove(peer.getId());
		}
		synchronized (idToPeersConnected) {
			idToPeersConnected.put(peer.getId(), peer);
		}
	}
	
	public void putPeerUndefinied(Peer peer) {
		synchronized (idToPeersUndefined) {
			idToPeersUndefined.put(peer.getId(), peer);
		}
	}
	
	public void putPeerConnected(Peer peer) {
		synchronized (idToPeersConnected) {
			idToPeersConnected.put(peer.getId(), peer);
		}
	}
	
	public void backToDownload(Integer index) {
		synchronized (onDonwloading) {
			onDonwloading.remove(index);
		}
		synchronized (nextDownloadPieces) {
			nextDownloadPieces.add(index);
		}
	}
	
	public void setMyBitfieldPiece(int index) {
		synchronized (myBitfield) {
			myBitfield.set(index);
		}
	}
	

	public Integer getPiecesLength() {
		return metaTorrent.getPiecesLength();
	}


	public boolean isCompleted(MetaFileTorrent metaFile) {
		return fileIsCompleted.get(metaFile);
	}
	
	@SuppressWarnings("unused")
	private void init() {
		
	}

	public void setPeerBitfield(Peer peer, int index) {
		synchronized (peerToBitfield) {
			peerToBitfield.get(peer).set(index);
		}
	}
	
	/**
	 * Recupera a razão de upload por download do torrent.
	 * @return razão de upload por download do torrent
	 */
	public Double getRatio() {
		return uploaded.doubleValue() / downloaded.doubleValue();
	}
	
	/**
	 * Recupera tamanho do torrent.
	 * @return tamanho do torrent
	 */
	public long getLenghtTorrent() {
		return metaTorrent.getLenghtTorrent();
	}
	
	/**
	 * Recupera número de pares do torrent.
	 * @return número de pares do torrent
	 */
	public int getNumPeers() {
		return idToPeers.size();
	}
	
	/**
	 * Recupera número de pares conectados do torrent.
	 * @return número de pares conectados do torrent
	 */
	public int getNumPeersConnected() {
		return idToPeersConnected.size();
	}
	
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
		return peerToBitfield;
	}
	public int getUploaded() {
		return uploaded.get();
	}
	public int getDownloaded() {
		return downloaded.get();
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
	public Map<MetaFileTorrent, Boolean> getFileIsCompleted() {
		return fileIsCompleted;
	}

	public void setFileIsCompleted(ConcurrentMap<MetaFileTorrent, Boolean> fileIsCompleted) {
		this.fileIsCompleted = fileIsCompleted;
	}

	public Map<MetaFileTorrent, FileLimits> getFileToLimits() {
		return fileToLimits;
	}

	public void setFileToLimits(ConcurrentMap<MetaFileTorrent, FileLimits> fileToLimits) {
		this.fileToLimits = fileToLimits;
	}

	// MÉTODOS PARA DEFINIÇÃO DE ATRIBUTOS
	public void setMe(Peer me) {
		this.me = me;
	}
	public void setIdToPeers(ConcurrentMap<ByteBuffer, Peer> idToPeers) {
		this.idToPeers = idToPeers;
	}
	public void setIdToPeersConnected(ConcurrentMap<ByteBuffer, Peer> idToPeersConnected) {
		this.idToPeersConnected = idToPeersConnected;
	}
	public void setIdToBitfield(ConcurrentMap<Peer, BitSet> idToBitfield) {
		this.peerToBitfield = idToBitfield;
	}
	public void setUploaded(int uploaded) {
		this.uploaded.set(uploaded);
	}
	public void setDownloaded(int downloaded) {
		this.downloaded.set(downloaded);
	}
	public void setMetaTorrent(MetaTorrent metaTorrent) {
		this.metaTorrent = metaTorrent;
	}
	public void setMyBitfield(BitSet myBitfield) {
		this.myBitfield = myBitfield;
	}
	public void setLastActivity(Date lastActivity) {
		this.lastActivity = lastActivity;
	}
	public void setSharePath(File sharePath) {
		this.sharePath = sharePath;
	}
	public boolean isPause() {
		return pause;
	}
	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public boolean isSeeder() {
		return seeder;
	}

	public void setSeeder(boolean seeder) {
		this.seeder = seeder;
	}

	public Map<ByteBuffer, Peer> getIdToPeersUndefined() {
		return idToPeersUndefined;
	}

	public void setIdToPeersUndefined(ConcurrentMap<ByteBuffer, Peer> idToPeersUndefined) {
		this.idToPeersUndefined = idToPeersUndefined;
	}
	

}
