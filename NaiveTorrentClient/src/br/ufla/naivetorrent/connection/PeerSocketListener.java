package br.ufla.naivetorrent.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.domain.peer.Peer;
import br.ufla.naivetorrent.peer.protocol.HandshakeMessage;

public class PeerSocketListener implements Runnable {

	public final static int MAX_CONNECTIONS = 15;
	public final static long TIME_VERIFY = 90000;
	@SuppressWarnings("unused")
	private Peer me;
	private ServerSocket socketListener;
	private ConcurrentHashMap<ByteBuffer, ShareTorrent> idToShareTorrent;
	private ConcurrentHashMap<ByteBuffer, ManagerConnections> idToManagerConnections;
	private ConcurrentHashMap<ByteBuffer, Boolean> idToStopConnection;
	private AtomicBoolean stop;
	
	// CONSTRUCTOR
	public PeerSocketListener(Peer me) throws IOException {
		this.me = me;
		socketListener = new ServerSocket(me.getSocketAddressListening().getPort());
		socketListener.setSoTimeout(10000);
		stop.set(false);
		idToShareTorrent = new ConcurrentHashMap<>();
		idToManagerConnections = new ConcurrentHashMap<>();
		idToStopConnection = new ConcurrentHashMap<>();
		new Thread(new VerifyConnections()).start();
	}
	
	public boolean isStop() {
		return stop.get();
	}
	
	public void setStop(boolean stop) {
		this.stop.set(stop);
	}

	public void putStateConnections(ByteBuffer hashTorrent, boolean value) {
		idToStopConnection.put(hashTorrent, value);
	}
	
	public boolean getStateConnections(ByteBuffer hashTorrent) {
		return idToStopConnection.get(hashTorrent);
	}
	
	public void putConnection(ByteBuffer infoHash, 
			PairedConnection connection) {
		idToManagerConnections.get(infoHash).putConnection(connection);
	}

	@Override
	public void run() {
		while (!isStop()) {
			try {
				Socket socket = socketListener.accept();
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				byte data[] = new byte[HandshakeMessage.MESSAGE_LENGTH + 1];
				if (in.read(data) != HandshakeMessage.MESSAGE_LENGTH) {
					in.close();
					socket.close();
					continue;
				} 
				byte handshake[] = new byte[HandshakeMessage.MESSAGE_LENGTH];
				PairedConnection.copyArray(handshake, data, 0, HandshakeMessage.MESSAGE_LENGTH);
				HandshakeMessage handshakeMessage = HandshakeMessage.decode(handshake);
				ShareTorrent shareTorrent = idToShareTorrent.get(handshakeMessage.getInfoHash());
				// ignora conexão
				if (shareTorrent == null) {
					in.close();
					socket.close();
					continue;
				}
				if (getStateConnections(handshakeMessage.getInfoHash())) {
					in.close();
					socket.close();
					continue;
				}
				// escrita bitfield
				BitSet myBitfield = shareTorrent.getMyBitfield();
				byte myBitfieldBytes[] =  myBitfield.toByteArray();
				int length = myBitfieldBytes.length;
				data = new byte[length + 5];
				int index = 0;
				index += PairedConnection.copyArray(data, length + 1, index);
				data[index++] = 5;
				index = PairedConnection.copyArray(data, myBitfield.toByteArray(), index);
				out.write(data);
				out.flush();
				// leitura bitfield
				byte peerBitfield[] = new byte[length + 5];
				int lengthRead = in.read(peerBitfield);
				if (lengthRead != length + 5) {
					in.close();
					out.close();
					socket.close();
					continue;
				}
				byte lenghtB[] = new byte[4];
				for (int i = 0; i < 4; i++) {
					lenghtB[i] = peerBitfield[i];
				}
				byte cod = peerBitfield[4];
				byte bitfield[] = new byte[length];
				for (int i = 0; i < length; i++) {
					bitfield[i] = peerBitfield[i + 5];
				}
				// fecha a conexão
				if (in.available() > 0 
						|| new BigInteger(lenghtB).intValue() != length + 1 
						|| cod != 5) {
					in.close();
					out.close();
					socket.close();
					continue;
				}
				Peer peer = shareTorrent.connectedPeer(handshakeMessage.getPeerId());
				shareTorrent.putPeerBitfield(peer, BitSet.valueOf(bitfield));
				PairedConnection newConnection = new PairedConnection(shareTorrent, peer, socket);
				putConnection(handshakeMessage.getInfoHash(), newConnection);
				new Thread(newConnection).start();
			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	class VerifyConnections implements Runnable {
		
		public void run() {
			while (!isStop()) {
				try {
					Thread.sleep(TIME_VERIFY);
					for (Map.Entry<ByteBuffer, ManagerConnections> entry :  
							idToManagerConnections.entrySet()) {
						if (entry.getValue().getNumConnections() >= MAX_CONNECTIONS) {
							idToStopConnection.put(entry.getKey(), true);
						} else {
							idToStopConnection.put(entry.getKey(), false);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
