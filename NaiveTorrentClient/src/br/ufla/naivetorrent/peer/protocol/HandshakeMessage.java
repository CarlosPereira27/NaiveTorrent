package br.ufla.naivetorrent.peer.protocol;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class HandshakeMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Byte pstrlen;
	private String pstr;
	private ByteBuffer reserved;
	private ByteBuffer infoHash;
	private ByteBuffer peerId;

}
