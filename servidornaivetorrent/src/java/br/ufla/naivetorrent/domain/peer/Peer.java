package br.ufla.naivetorrent.domain.peer;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import br.ufla.naivetorrent.util.UtilHex;

public class Peer implements Serializable {

	private static final long serialVersionUID = 1L;
	private ByteBuffer id;
	private InetSocketAddress socketAddressListening;
	
	public String getIpOrHostName() {
		if (socketAddressListening == null)
			return null;
		return socketAddressListening.getAddress().getHostAddress();
	}
	
	public Integer getPort() {
		if (socketAddressListening == null)
			return null;
		return socketAddressListening.getPort();
	}
	
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
		this.id =  UtilHex.toBytes(idHex);
	}
	public InetSocketAddress getSocketAddressListening() {
		return socketAddressListening;
	}
	public void setSocketAddressListening(InetSocketAddress socketAddressListening) {
		this.socketAddressListening = socketAddressListening;
	}
	
	
	// HASHCODE e EQUALS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((socketAddressListening == null) ? 0 : socketAddressListening.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Peer other = (Peer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (socketAddressListening == null) {
			if (other.socketAddressListening != null)
				return false;
		} else if (!socketAddressListening.equals(other.socketAddressListening))
			return false;
		return true;
	}

	
	// TOSTRING
	@Override
	public String toString() {
		return "Peer [id=" + id + ", socketAddressListening=" + socketAddressListening + "]";
	}
	
}
