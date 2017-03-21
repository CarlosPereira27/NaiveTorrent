package br.ufla.naivetorrent.domain.tracker;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import br.ufla.naivetorrent.util.UtilHex;

public class Tracker implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ByteBuffer id;
	private InetSocketAddress socketAddressListening;
	
	public ByteBuffer getId() {
		return id;
	}
	public String getIdHex() {
		return UtilHex.toHexString(id);
	}
	public void setId(ByteBuffer id) {
		this.id = id;
	}
	public void setIdHex(String idHex) {
		this.id = UtilHex.toBytes(idHex);
	}
	public InetSocketAddress getSocketAddressListening() {
		return socketAddressListening;
	}
	public void setSocketAddressListening(InetSocketAddress socketAddressListening) {
		this.socketAddressListening = socketAddressListening;
	}
	

}
