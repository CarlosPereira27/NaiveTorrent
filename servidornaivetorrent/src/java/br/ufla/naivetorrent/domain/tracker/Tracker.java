package br.ufla.naivetorrent.domain.tracker;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import br.ufla.naivetorrent.util.UtilHex;

public class Tracker implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ByteBuffer id;
	private InetSocketAddress addressListening;
	
	/**
	 * Recupera o endereço do tracker.
	 * @return endereço do tracker
	 */
	public String getAddress() {
		return addressListening.getHostName() + ":"
				+ addressListening.getPort();
	}
	
	public void setAddressListeningString(String addressListStr) {
		String[] tokens = addressListStr.split(":");
		String ip = tokens[0];
		int port = Integer.parseInt(tokens[1]);
		addressListening = new InetSocketAddress(ip, port);
	}
	
	public String getIpOrHostName() {
		if (addressListening == null)
			return null;
		return addressListening.getAddress().getHostAddress();
	}
	
	public Integer getPort() {
		if (addressListening == null)
			return null;
		return addressListening.getPort();
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
	public InetSocketAddress getAddressListening() {
		return addressListening;
	}
	public void setAddressListening(InetSocketAddress addressListening) {
		this.addressListening = addressListening;
	}
	
	
	// HASHCODE e EQUALS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((addressListening == null) ? 0 : addressListening.hashCode());
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
		if (addressListening == null) {
			if (other.addressListening != null)
				return false;
		} else if (!addressListening.equals(other.addressListening))
			return false;
		return true;
	}

	
	// TOSTRING
	@Override
	public String toString() {
		return "Tracker [id=" + id + ", socketAddressListening=" + addressListening + "]";
	}
	

}
