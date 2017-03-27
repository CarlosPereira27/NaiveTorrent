package br.ufla.naivetorrent.domain.file;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import br.ufla.naivetorrent.connection.ManagerConnections;
import br.ufla.naivetorrent.connection.PeerSocketListener;
import br.ufla.naivetorrent.domain.peer.Peer;
import br.ufla.naivetorrent.domain.tracker.Tracker;
import br.ufla.naivetorrent.peer.protocol.DownloadStrategy;
import br.ufla.naivetorrent.peer.protocol.HandshakeHandler;
import br.ufla.naivetorrent.tracker.request.ManagerTorrentRequest;
import br.ufla.naivetorrent.util.UtilByteString;

public class ShareTorrent {
	
	public static final int MAX_DOWNLOADING = 15;
	public static final int MAX_CONNECTIONS = 15;
	
	class SimpleManagerConnection implements Runnable {

		@Override
		public void run() {
			while (!isPause() && !isSeeder()) {
				if (peersConnected.size() < MAX_CONNECTIONS && !peers.isEmpty()) {
					new Thread(new HandshakeHandler(me, nextPeer(), 
							ShareTorrent.this, peerSocketListener)).start();
				}
			}
			
		}
		
	}
	
	class SimpleManagerDownload implements Runnable {

		@Override
		public void run() {
			while (!isPause() && !isSeeder()) {
				if (nextDownloadPieces.size() < DownloadStrategy.NUM_PIECES) {
					getMoreRarestPieces();
				}
				if (!nextDownloadPieces.isEmpty() && 
						onDonwloading.size() < MAX_DOWNLOADING) {
					int pieceIndex = getNextPiece();
					myBitfieldNextDownloading.set(pieceIndex);
					onDonwloading.add(pieceIndex);
					Peer peer = getPeerHave(pieceIndex);
					managerConnections.downloadingPiece(pieceIndex, peer);
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	public Peer getPeerHave(int pieceIndex) {
		List<Peer> peersHave = new ArrayList<>();
		for (Peer peer : peersConnected) {
			peersHave.add(peer);
		}
		int index = random.nextInt(peersHave.size());
		return peersHave.get(index);
	}
	
	public int getNextPiece() {
		return nextDownloadPieces.remove(0);
	}
	
	public Peer nextPeer() {
		Iterator<Peer> it = peers.iterator();
		Peer peer = it.next();
		it.remove();
		return peer;
	}
	

	private ManagerTorrentRequest managerTorrentRequest;
	private ManagerConnections managerConnections;
	private PeerSocketListener peerSocketListener;
	private Peer me;
	private DownloadStrategy downloadStrategy;
	private List<Integer> nextDownloadPieces;
	private List<Integer> onDonwloading;
	private Map<MetaFileTorrent, Boolean> fileIsCompleted;
	private Map<MetaFileTorrent, FileLimits> fileToLimits;
	private ConcurrentMap<ByteBuffer, Peer> idToPeers;
	private Set<Peer> peers;
	private Set<Peer> peersConnected;
	private ConcurrentMap<Peer, BitSet> peerToBitfield;
	private AtomicLong uploaded;
	private AtomicLong downloaded;
	private AtomicLong left;
	private MetaTorrent metaTorrent;
	private BitSet myBitfield;
	private BitSet myBitfieldNextDownloading;
	private Date lastActivity;
	private File sharePath;
	private AtomicBoolean pause;
	private AtomicBoolean seeder;
	private Random random = new Random();
	
	public ShareTorrent() {
		downloadStrategy = new DownloadStrategy(this);
		nextDownloadPieces = new CopyOnWriteArrayList<>();
		onDonwloading = new CopyOnWriteArrayList<>();
		fileIsCompleted = new HashMap<>();
		fileToLimits = new HashMap<>();
		idToPeers = new ConcurrentHashMap<>();
		peers = new CopyOnWriteArraySet<>();
		peersConnected = new CopyOnWriteArraySet<>();
		peerToBitfield = new ConcurrentHashMap<>();
		uploaded = new AtomicLong();
		downloaded = new AtomicLong();
		left = new AtomicLong();
	}
	
	private void setMetaFileData() {
		List<MetaFileTorrent> metaFiles = metaTorrent.getInfo().getMetaFiles();
		long limit = 0;
		for (MetaFileTorrent metaFile : metaFiles) {
			fileToLimits.put(metaFile, 
					new FileLimits(limit, limit + metaFile.getLength()));
			fileIsCompleted.put(metaFile, false);
			limit += metaFile.getLength();
		}
		int numPieces = getNumPieces();
		myBitfield = new BitSet(numPieces);
		myBitfieldNextDownloading = new BitSet(numPieces);
	}
	
	public int getNumPieces() {
		long torrentLength = metaTorrent.getLenghtTorrent();
		long piecesLength = metaTorrent.getPiecesLength();
		int numPieces = (int) (torrentLength / piecesLength);
		if (torrentLength % piecesLength != 0) {
			numPieces++;
		}
		return numPieces;
	}
	
	public BitSet getMyBitfieldWithNext() {
		BitSet myBitfieldCp;
		synchronized (myBitfield) {
			myBitfieldCp = (BitSet) myBitfield.clone();
		}
		synchronized (myBitfieldNextDownloading) {
			myBitfieldCp.or(myBitfieldNextDownloading);
		}
		return myBitfieldCp;
	}
	
	public void getMoreRarestPieces() {
		nextDownloadPieces.addAll(downloadStrategy.getPieces());
	}
	
	public void putPeerBitfield(Peer peer, BitSet bitfield) {
		peerToBitfield.put(peer, bitfield);
	}
	
	
	public Peer connectedPeer(ByteBuffer peerId) {
		Peer peer = idToPeers.get(peerId);
		if (peer == null) {
			peer = new Peer();
			peer.setId(peerId);
			peersConnected.add(peer);
		} else {
			peers.remove(peer);
			peersConnected.add(peer);
		}
		return peer;
	}
	
	public void disconectedPeer(Peer peer) {
		peers.remove(peer);
		peersConnected.remove(peer);
		peerToBitfield.remove(peer);
	}
	
	public void definiedPeer(Peer peer) {
		peersConnected.add(peer);
	}
	
	public void connectedPeer(Peer peer) {
		peersConnected.add(peer);
	}
	
	public void backToDownload(Integer index) {
		onDonwloading.remove(index);
		nextDownloadPieces.add(index);
		myBitfieldNextDownloading.set(index, false);
	}
	
	public void setMetaFileCompleted(MetaFileTorrent metaFile) {
		String pathFile = sharePath.getPath() + metaFile.getPathFile() + ".part";
		String newPathFile = sharePath.getPath() + metaFile.getPathFile();
		File file = new File(pathFile);
		file.renameTo(new File(newPathFile));
		fileIsCompleted.put(metaFile, true);
	}
	
	public void verifyFileCompleted(MetaFileTorrent metaFile, FileLimits fileLimits) {
		long pieceLength = getPiecesLength();
		int indexMin = (int) (fileLimits.limitInf / pieceLength);
		if (fileLimits.limitInf % pieceLength == 0) {
			indexMin--;
		}
		int indexMax = (int) (fileLimits.limitSup / pieceLength);
		synchronized (myBitfield) {
			for (int i = indexMin; i <= indexMax; i++) {
				if (!myBitfield.get(i)) {
					return;
				}
			}
		}
		setMetaFileCompleted(metaFile);
	}
	
	public void verifyFileCompleted(int index) {
		long pointerIndex = index * getPiecesLength();
		long nextPointerIndex = pointerIndex + getPiecesLength();
		for (Map.Entry<MetaFileTorrent, FileLimits> entry : fileToLimits.entrySet()) {
			FileLimits fileLimits = entry.getValue();
			if (fileLimits.limitSup > pointerIndex 
					&& fileLimits.limitSup < nextPointerIndex) {
				verifyFileCompleted(entry.getKey(), fileLimits);
			} else if (entry.getValue().limitInf > pointerIndex
					&& fileLimits.limitSup < nextPointerIndex) {
				verifyFileCompleted(entry.getKey(), fileLimits);
			} else if (entry.getValue().limitInf > nextPointerIndex) {
				return;
			}
		}
	}
	
	public void setSimpleMyBitfieldPiece(int index) {
		synchronized (myBitfield) {
			myBitfield.set(index);
		}
	}
	
	public void setMyBitfieldPiece(int index) {
		onDonwloading.remove(new Integer(index));
		synchronized (myBitfield) {
			myBitfield.set(index);
		}
		managerConnections.multicastHave(index);
		verifyFileCompleted(index);
	}
	
	public FileLimits getFileLimits(MetaFileTorrent metaFileTorrent) {
		return fileToLimits.get(metaFileTorrent);
	}
	

	public Integer getPiecesLength() {
		return metaTorrent.getPiecesLength();
	}


	public boolean isCompleted(MetaFileTorrent metaFile) {
		return fileIsCompleted.get(metaFile);
	}
	

	public void init() {
		new Thread(new SimpleManagerConnection()).start();
		new Thread(new SimpleManagerDownload()).start();
	}

	public void setPeerBitfield(Peer peer, int index) {
		BitSet peerBitfield = peerToBitfield.get(peer);
		synchronized (peerBitfield) {
			peerBitfield.set(index);
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
		return peers.size();
	}
	
	/**
	 * Recupera número de pares conectados do torrent.
	 * @return número de pares conectados do torrent
	 */
	public int getNumPeersConnected() {
		return peersConnected.size();
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
			myBitfield = BitSet.valueOf(UtilByteString
					.stringToBytes(myBitfieldString));
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
	public Set<Peer> getPeers() {
		return peers;
	}
	public Set<Peer> getPeersConnected() {
		return peersConnected;
	}
	public Map<Peer, BitSet> getIdToBitfield() {
		return peerToBitfield;
	}
	public long getUploaded() {
		return uploaded.get();
	}
	public long getDownloaded() {
		return downloaded.get();
	}
	public long getLeft() {
		return left.get();
	}
	public String getInfoHashHex() {
		return metaTorrent.getInfoHashHex();
	}


	public List<Tracker> getTrackers() {
		return metaTorrent.getTrackers();
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
	public void setPeers(Set<Peer> peers) {
		this.peers = peers;
	}
	public void setPeersConnected(Set<Peer> peersConnected) {
		this.peersConnected = peersConnected;
	}
	public void setIdToBitfield(ConcurrentMap<Peer, BitSet> idToBitfield) {
		this.peerToBitfield = idToBitfield;
	}
	public void setUploaded(long uploaded) {
		this.uploaded.set(uploaded);
	}
	public void setDownloaded(long downloaded) {
		this.downloaded.set(downloaded);
	}
	public void setLeft(long left) {
		this.left.set(left);
	}
	public void setMetaTorrent(MetaTorrent metaTorrent) {
		this.metaTorrent = metaTorrent;
		setMetaFileData();
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
		return pause.get();
	}
	public void setPause(boolean pause) {
		this.pause.set(pause);
	}

	public boolean isSeeder() {
		return seeder.get();
	}

	public void setSeeder(boolean seeder) {
		this.seeder.set(seeder);
	}
	
	public ManagerConnections getManagerConnections() {
		return managerConnections;
	}

	public void setManagerConnections(ManagerConnections managerConnections) {
		this.managerConnections = managerConnections;
	}

	public PeerSocketListener getPeerSocketListener() {
		return peerSocketListener;
	}

	public void setPeerSocketListener(PeerSocketListener peerSocketListener) {
		this.peerSocketListener = peerSocketListener;
	}

	public ManagerTorrentRequest getManagerTorrentRequest() {
		return managerTorrentRequest;
	}

	public void setManagerTorrentRequest(ManagerTorrentRequest managerTorrentRequest) {
		this.managerTorrentRequest = managerTorrentRequest;
	}


}
