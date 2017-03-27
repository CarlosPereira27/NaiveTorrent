package br.ufla.naivetorrent.connection;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.domain.peer.Peer;

public class ManagerConnections {
	
	@SuppressWarnings("unused")
	private PeerSocketListener listener;
	private ShareTorrent shareTorrent;
	private ConcurrentMap<Peer, PairedConnection> connections;
	
	public ManagerConnections(PeerSocketListener listener, ShareTorrent shareTorrent) {
		this.listener = listener;
		this.shareTorrent = shareTorrent;
		connections = new ConcurrentHashMap<>();
	}

	public int getNumConnections() {
		return connections.size();
	}
	
	public void putConnection(PairedConnection connection) {
		connections.put(connection.getPeer(), connection);
	}
	
	
	public void removeConnection(PairedConnection connection) {
		connections.remove(connection);
	}
	
	public void downloadingPiece(int index, Peer peer) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				PairedConnection.Message message = new PairedConnection.Message();
				message.id = 6;
				message.index = index;
				message.begin = 0;
				message.length = shareTorrent.getPiecesLength();
				connections.get(peer).addSendMessage(message);
			}
		}).start();
	}
	
	public void multicastHave(int index) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				PairedConnection.Message message = new PairedConnection.Message();
				message.id = 4;
				message.index = index;
				Collection<PairedConnection> connectionsSet = connections.values();
				for (PairedConnection connection : connectionsSet) {
					connection.addSendMessage(message);
				}
			}
		}).start();
	}

}
