package br.ufla.naivetorrent.peer.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.BitSet;

import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.domain.peer.Peer;

public class HandshakeHandler {
	
	private Peer me;
	private Peer peer;
	private InetSocketAddress socketAddress;
	private ShareTorrent shareTorrent;
	
	public HandshakeHandler(Peer me, Peer peer, 
			InetSocketAddress socketAddress, 
			ShareTorrent shareTorrent) {
		this.me = me;
		this.peer = peer;
		this.socketAddress = socketAddress;
		this.shareTorrent = shareTorrent;
	}
	
	private HandshakeHandler() {
		
	}

	public void sendHandshakeMessage() 
			throws UnknownHostException, IOException {
		Socket socket = new Socket(socketAddress.getHostName(), 
				socketAddress.getPort());
		socket.connect(peer.getSocketAddressListening());
		OutputStream out = socket.getOutputStream();
		out.write(new HandshakeMessage(shareTorrent
				.getMetaTorrent().getInfoHash(), 
				me.getId()).toByteArray());
		out.flush();
		BitSet myBitfield = shareTorrent.getMyBitfield();
		byte myBitfieldBytes[] =  myBitfield.toByteArray();
		int length = myBitfieldBytes.length;
		byte peerBitfield[] = new byte[length + 1];
		InputStream in = socket.getInputStream();
		if (in.read(peerBitfield) != length) {
			socket.close();
		} else {
			peerBitfield = Arrays.copyOf(peerBitfield, length);
			out.write(myBitfieldBytes);
			out.flush();
			// open connection
		}
	}
	
	public void receiveHandshakeMessage() {
		
	}
	
	
	
	private void setMe(Peer me) {
		this.me = me;
	}
	private void setPeer(Peer peer) {
		this.peer = peer;
	}
	private void setSocketAddress(InetSocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}
	private void setShareTorrent(ShareTorrent shareTorrent) {
		this.shareTorrent = shareTorrent;
	}
	private boolean isReadyToBuild() {
		return peer != null && socketAddress != null && shareTorrent != null;
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
		 * Define o endereço do socket usado para realizar o handshake.
		 * @param metaTorrent  endereço do socket
		 * @return próprio builder
		 */
		public Builder withSocketAddress(InetSocketAddress socketAddress) {
			handshakeHandler.setSocketAddress(socketAddress);
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
