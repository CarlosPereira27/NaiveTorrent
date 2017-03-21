package br.ufla.naivetorrent.peer.protocol;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;

import br.ufla.naivetorrent.domain.peer.Peer;

public class HandshakeHandler {
	
	private Peer peer;
	private SocketAddress socketAddress;
	
	public void sendHandshakeMessage() {
		Socket socket = new Socket();
	}

}
