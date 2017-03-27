package br.ufla.naivetorrent.peer.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.BitSet;

import br.ufla.naivetorrent.connection.PairedConnection;
import br.ufla.naivetorrent.connection.PeerSocketListener;
import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.domain.peer.Peer;

public class HandshakeHandler implements Runnable {
	
	private Peer me;
	private Peer peer;
	private ShareTorrent shareTorrent;
	private PeerSocketListener peerSocketListener;
	
	public HandshakeHandler(Peer me, Peer peer, 
			ShareTorrent shareTorrent, PeerSocketListener peerSocketListener) {
		this.me = me;
		this.peer = peer;
		this.shareTorrent = shareTorrent;
		this.peerSocketListener = peerSocketListener;
	}
	
	private HandshakeHandler() {
		
	}

	public void sendHandshakeMessage() 
			throws UnknownHostException, IOException {
		Socket socket = new Socket(peer.getIpOrHostName(), 
				peer.getPort());
		OutputStream out = socket.getOutputStream();
		out.write(new HandshakeMessage(shareTorrent
				.getMetaTorrent().getInfoHash(), 
				me.getId()).toByteArray());
		out.flush();
		// read bitfield message
		byte myBitfield[] =  shareTorrent.getMyBitfield().toByteArray();
		int length = myBitfield.length;
		byte peerBitfield[] = new byte[length + 5];
		InputStream in = socket.getInputStream();
		int lengthRead = in.read(peerBitfield);
		if (lengthRead != length + 5) {
			in.close();
			out.close();
			socket.close();
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
			return;
		} 
		// send my bitfield
		byte data[] = new byte[length + 5];
		int index = 0;
		index += PairedConnection.copyArray(data, length + 1, index);
		data[index++] = 5;
		index = PairedConnection.copyArray(data, myBitfield, index);
		out.write(data);
		out.flush();
		// abre conexão
		shareTorrent.connectedPeer(peer);
		shareTorrent.putPeerBitfield(peer, BitSet.valueOf(bitfield));
		PairedConnection newConnection = new PairedConnection(shareTorrent, peer, socket);
		peerSocketListener.putConnection(shareTorrent.getMetaTorrent().getInfoHash(),
				newConnection);
		new Thread(newConnection).start();
	}

	@Override
	public void run() {
		try {
			sendHandshakeMessage();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void setMe(Peer me) {
		this.me = me;
	}
	private void setPeer(Peer peer) {
		this.peer = peer;
	}
	private void setShareTorrent(ShareTorrent shareTorrent) {
		this.shareTorrent = shareTorrent;
	}
	private boolean isReadyToBuild() {
		return peer != null  && shareTorrent != null;
	}


	/**
	 * Construtor de HandshakeHandler.
	 * @author carlos
	 *
	 */
	public class Builder {
		
		private HandshakeHandler handshakeHandler;
		
		public Builder() {
			handshakeHandler = new HandshakeHandler();
		}
		
		/**
		 * Define o peer que deseja conectar-se.
		 * @param peer peer que deseja conectar-se
		 * @return próprio builder
		 */
		public Builder withMe(Peer me) {
			handshakeHandler.setMe(me);
			return this;
		}
		
		/**
		 * Define o peer que deseja conectar-se.
		 * @param peer peer que deseja conectar-se
		 * @return próprio builder
		 */
		public Builder withPeer(Peer peer) {
			handshakeHandler.setPeer(peer);
			return this;
		}
		
		/**
		 * Define o arquivo de metadados do arquivo que deseja compartilhar.
		 * @param metaTorrent  arquivo de metadados
		 * @return próprio builder
		 */
		public Builder withShareTorrent(ShareTorrent shareTorrent) {
			handshakeHandler.setShareTorrent(shareTorrent);
			return this;
		}
		
		/**
		 * Constrói o objeto HandshakeHandler
		 * @return objeto HandshakeHandler
		 */
		public HandshakeHandler build() {
			if (!isReadyToBuild())
				throw new RuntimeException("HandshakeHandler não está "
						+ "pronto para ser construído!");
			return handshakeHandler;
		}
		
	}




}
