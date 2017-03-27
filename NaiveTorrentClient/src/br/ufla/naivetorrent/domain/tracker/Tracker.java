package br.ufla.naivetorrent.domain.tracker;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import br.ufla.naivetorrent.util.UtilHex;

public class Tracker implements Serializable, Comparable<Tracker> {
	
	private static final long serialVersionUID = 1L;
	public static final int MIN_INTERVAL = 60;
	private ByteBuffer id;
	private InetSocketAddress addressListening;
	private AtomicInteger interval;
	
	public Tracker() {
		interval = new AtomicInteger(MIN_INTERVAL);
	}
	
	/**
	 * Recupera o endereço do tracker.
	 * @return endereço do tracker
	 */
	public String getAddress() {
		return addressListening.getHostName() + ":"
				+ addressListening.getPort();
	}
	
	public void setAddressListeningString(String addressListStr) 
			throws Exception {
		String[] tokens = addressListStr.split(":");
		if (tokens.length != 2) {
			throw new Exception(
					"Erro! O endereço do tracker ('" 
					+ addressListStr 
					+ "') está mal definido.\n" 
					+ "Formato correto: <ip>:<porta>"
					);
		}
		String ip = tokens[0];
		int port;
		try {
			port = Integer.parseInt(tokens[1]);
		} catch (NumberFormatException e) {
			throw new Exception(
					"Erro! O número da porta do tracker ('"
					+ addressListStr 
					+ "') está mal definido (deve ser um número inteiro."
					);
		}
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
	public int getInterval() {
		return interval.get();
	}

	public void setInterval(int interval) {
		this.interval.set(interval);
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

	@Override
	public int compareTo(Tracker o) {
		return interval.get() - o.interval.get();
	}


	

}
