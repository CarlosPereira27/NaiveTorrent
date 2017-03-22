package br.ufla.naivetorrent.tracker.protocol;

import java.io.Serializable;

/**
 * Representa um mensagem enviada para o tracker.
 * @author carlos
 *
 */
public class RequestMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * Hash que identifica o arquivo.
	 */
	private String infoHash;
	/**
	 * Hash que identifica o peer.
	 */
	private String peerId;
	/**
	 * IP onde o peer está escutando.
	 */
	private String ip;
	/**
	 * Porta onde o peer está escutando.
	 */
	private Integer port;
	/**
	 * Quantidade de upload (bytes) realizada no arquivo.
	 */
	private Long uploaded;
	/**
	 * Quantidade de download (bytes) realizada no arquivo.
	 */
	private Long downloaded;
	/**
	 * Quantidade de bytes que restam para terminar o arquivo.
	 */
	private Long left;
	/**
	 * Evento de requisição, pode estar iniciando comunicação, parando ou informando
	 * que tornou um seeder.
	 */
	private RequestEvent event;
	/**
	 * Quantidade de peers que deseja receber do tracker.
	 */
	private Integer numWant;
	/**
	 * Hash que identifica o tracker.
	 */
	private String trackerId;
	
	/**
	 * Verifica se a mensagem da requisição é válida.
	 * @return true se a mensagem da requisição é válida, caso contrário false.
	 */
	public boolean isValid() {
		return infoHash != null && peerId != null && port != null
				&& uploaded != null && downloaded != null
				&& left != null;
	}
	
	// MÉTODOS ACESSORES
	public String getInfoHash() {
		return infoHash;
	}
	public void setInfoHash(String infoHash) {
		this.infoHash = infoHash;
	}
	public String getPeerId() {
		return peerId;
	}
	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Long getUploaded() {
		return uploaded;
	}
	public void setUploaded(Long uploaded) {
		this.uploaded = uploaded;
	}
	public Long getDownloaded() {
		return downloaded;
	}
	public void setDownloaded(Long downloaded) {
		this.downloaded = downloaded;
	}
	public Long getLeft() {
		return left;
	}
	public void setLeft(Long left) {
		this.left = left;
	}
	public RequestEvent getEvent() {
		return event;
	}
	public void setEvent(RequestEvent event) {
		this.event = event;
	}
	public Integer getNumWant() {
		return numWant;
	}
	public void setNumWant(Integer numWant) {
		this.numWant = numWant;
	}
	public String getTrackerId() {
		return trackerId;
	}
	public void setTrackerId(String trackerId) {
		this.trackerId = trackerId;
	}

	
	// HASHCODE e EQUALS 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((downloaded == null) ? 0 : downloaded.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((infoHash == null) ? 0 : infoHash.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((numWant == null) ? 0 : numWant.hashCode());
		result = prime * result + ((peerId == null) ? 0 : peerId.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + ((trackerId == null) ? 0 : trackerId.hashCode());
		result = prime * result + ((uploaded == null) ? 0 : uploaded.hashCode());
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
		RequestMessage other = (RequestMessage) obj;
		if (downloaded == null) {
			if (other.downloaded != null)
				return false;
		} else if (!downloaded.equals(other.downloaded))
			return false;
		if (event != other.event)
			return false;
		if (infoHash == null) {
			if (other.infoHash != null)
				return false;
		} else if (!infoHash.equals(other.infoHash))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (numWant == null) {
			if (other.numWant != null)
				return false;
		} else if (!numWant.equals(other.numWant))
			return false;
		if (peerId == null) {
			if (other.peerId != null)
				return false;
		} else if (!peerId.equals(other.peerId))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		if (trackerId == null) {
			if (other.trackerId != null)
				return false;
		} else if (!trackerId.equals(other.trackerId))
			return false;
		if (uploaded == null) {
			if (other.uploaded != null)
				return false;
		} else if (!uploaded.equals(other.uploaded))
			return false;
		return true;
	}

	// TOSTRING
	@Override
	public String toString() {
		return "RequestMessage [infoHash=" + infoHash + ", peerId=" + peerId + ", ip=" + ip + ", port=" + port
				+ ", uploaded=" + uploaded + ", downloaded=" + downloaded + ", left=" + left + ", event=" + event
				+ ", numWant=" + numWant + ", trackerId=" + trackerId + "]";
	}
	

	
}
