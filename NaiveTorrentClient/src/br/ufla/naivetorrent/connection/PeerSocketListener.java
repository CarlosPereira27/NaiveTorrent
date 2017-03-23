package br.ufla.naivetorrent.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Map;

import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.domain.peer.Peer;
import br.ufla.naivetorrent.peer.protocol.HandshakeMessage;

public class PeerSocketListener implements Runnable {

	public final static int MAX_CONNECTIONS = 15;
	public final static long TIME_VERIFY = 90000;
	private Peer me;
	private ServerSocket serverSocket;
	private Map<ByteBuffer, ShareTorrent> idToShareTorrent;
	private Map<ByteBuffer, ManagerConnections> idToManagerConnections;
	private Map<ByteBuffer, Boolean> idToStopConnection;
	
	class VerifyConnections implements Runnable {
		
		public void run() {
			while (true) {
				try {
					Thread.sleep(TIME_VERIFY);
					synchronized (idToStopConnection) {
						for (Map.Entry<ByteBuffer, ManagerConnections> entry :  
								idToManagerConnections.entrySet()) {
							if (entry.getValue().getNumConnections() >= MAX_CONNECTIONS) {
								idToStopConnection.put(entry.getKey(), true);
							} else {
								idToStopConnection.put(entry.getKey(), false);
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public PeerSocketListener(Peer me) throws IOException {
		this.me = me;
		serverSocket = new ServerSocket(me.getSocketAddressListening().getPort());
		serverSocket.setSoTimeout(10000);
		new Thread(new VerifyConnections()).start();
	}
	
	public void setStateConnections(ByteBuffer hashTorrent, boolean value) {
		synchronized (idToStopConnection) {
			idToStopConnection.put(hashTorrent, value);
		}
	}
	
	public boolean getStateConnections(ByteBuffer hashTorrent) {
		synchronized (idToStopConnection) {
			return idToStopConnection.get(hashTorrent);
		}
	}
	
	
	

	@Override
	public void run() {
		while (true) {
			try {
				Socket server = serverSocket.accept();
				InputStream in = server.getInputStream();
				byte data[] = new byte[HandshakeMessage.MESSAGE_LENGTH + 1];
				if (in.read(data) != HandshakeMessage.MESSAGE_LENGTH) {

				} else {
					HandshakeMessage handshakeMessage = HandshakeMessage.decode(data);
					ShareTorrent shareTorrent = idToShareTorrent.get(handshakeMessage.getInfoHash());
					// ignora conexão
					if (shareTorrent == null) {
						continue;
					}
					if (getStateConnections(handshakeMessage.getInfoHash())) {
						server.close();
						continue;
					}
					Peer peer = shareTorrent.getIdToPeers().get(handshakeMessage.getPeerId());
					if (peer != null) {
						shareTorrent.putPeerConnected(peer);
					} else {
						peer = new Peer();
						peer.setId(handshakeMessage.getPeerId());
						shareTorrent.putPeerUndefinied(peer);
					}
					PairedConnection newConnection = new PairedConnection(shareTorrent, peer, server);
					new Thread(newConnection).start();
					idToManagerConnections.get(handshakeMessage.getInfoHash())
						.putConnection(newConnection);
				}

				in.close();

			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

}
