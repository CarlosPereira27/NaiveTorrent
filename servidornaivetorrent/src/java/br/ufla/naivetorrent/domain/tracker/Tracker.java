package br.ufla.naivetorrent.domain.tracker;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import br.ufla.naivetorrent.util.UtilHex;

public class Tracker implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ByteBuffer id;
	private InetSocketAddress socketAddressListening;
	
	/**
	 * Recupera o endereço do tracker.
	 * @return endereço do tracker
	 */
	public String getAddress() {
		return socketAddressListening.getHostName() + ":"
				+ socketAddressListening.getPort();
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
		this.id = UtilHex.toBytes(idHex);
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
		Tracker other = (Tracker) obj;
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
		return "Tracker [id=" + id + ", socketAddressListening=" + socketAddressListening + "]";
	}
	

}
