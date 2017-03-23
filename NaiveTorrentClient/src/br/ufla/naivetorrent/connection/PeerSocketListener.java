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
	
	private Peer me;
	private ServerSocket serverSocket;
	private Map<ByteBuffer, ShareTorrent> idToShareTorrent;
	
	public PeerSocketListener(Peer me) 
			throws IOException {
		this.me = me;
		serverSocket = new ServerSocket(
				me.getSocketAddressListening().getPort());
		serverSocket.setSoTimeout(10000);
	}

	@Override
	public void run() {
		while(true) {
         try {
            Socket server = serverSocket.accept();
            InputStream in = server.getInputStream();
            byte data[] = new byte[HandshakeMessage.MESSAGE_LENGTH + 1];
            if (in.read(data) != HandshakeMessage.MESSAGE_LENGTH) {
            	
            } else {
            	HandshakeMessage handshakeMessage = HandshakeMessage.decode(data);
            	ShareTorrent shareTorrent = idToShareTorrent.get(
            			handshakeMessage.getInfoHash());
            	if (shareTorrent == null) {
            		continue;
            	}
            	
            	Peer peer = shareTorrent.getIdToPeers()
            			.get(handshakeMessage.getPeerId());
            	if (peer != null) {
            		shareTorrent.putPeerConnected(peer);
            	} else {
            		peer = new Peer();
            		peer.setId(handshakeMessage.getPeerId());
            	}
            	if (shareTorrent != null) {
            		new Thread(new PairedConnection(shareTorrent));
            	}
            }
            
            in.close();
            
            
         }catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e) {
            e.printStackTrace();
            break;
         }
      }
	}

}
