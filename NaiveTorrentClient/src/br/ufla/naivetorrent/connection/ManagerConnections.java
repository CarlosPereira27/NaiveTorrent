package br.ufla.naivetorrent.connection;

import java.util.Map;

import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.domain.peer.Peer;

public class ManagerConnections {
	
	private PeerSocketListener listener;
	private ShareTorrent shareTorrent;
	private Map<Peer, PairedConnection> connections;
	
	
	public int getNumConnections() {
		synchronized (connections) {
			return connections.size();
		}
	}
	
	public void putConnection(PairedConnection connection) {
		synchronized (connections) {
			connections.put(connection.getPeer(), connection);
		}
	}
	
	
	public void removeConnection(PairedConnection connection) {
		synchronized (connections) {
			connections.remove(connection);
		}
	}
	
	public void downloadingPiece(int index, Peer peer) {
		synchronized (connections) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					connections.get(peer).requestPiece(index, 0, 
							shareTorrent.getPiecesLength());
					
				}
			}).start();
		}
	}

}
