package br.ufla.naivetorrent.connection;

import java.net.Socket;

import br.ufla.naivetorrent.domain.file.ShareTorrent;

public class PairedConnection {
	
	private ShareTorrent shareTorrent;
	private Socket socket;
	
	
	public PairedConnection(ShareTorrent shareTorrent, Socket socket) {
		this.shareTorrent = shareTorrent;
		this.socket = socket;
	}
	
	

}
