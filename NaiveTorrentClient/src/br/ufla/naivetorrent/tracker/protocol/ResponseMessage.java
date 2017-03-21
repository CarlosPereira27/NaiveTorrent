package br.ufla.naivetorrent.tracker.protocol;

import java.io.Serializable;
import java.util.List;

import br.ufla.naivetorrent.domain.peer.Peer;

/**
 * Representa uma resposta do tracker.
 * @author carlos
 *
 */
public class ResponseMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * Intervalo em segundos que o cliente deverá esperar para comunicar com o tracker.
	 */
	private Integer interval;
	/**
	 * Intervalo mínimo em segundos que o cliente deverá esperar para comunicar com o tracker.
	 */
	private Integer minInterval;
	/**
	 * Id do tracker.
	 */
	private String trackerId;
	/**
	 * Quantidade dos peers que possuem o arquivo inteiro ('seeders').
	 */
	private Integer complete;
	/**
	 * Quantidade dos peers que não possuem o arquivo inteiro ('leechers').
	 */
	private Integer incomplete;
	/**
	 * Lista de peers
	 */
	private List<Peer> peers;
	/**
	 * Id do cliente.
	 */
	private String clientId;
	
	
	/**
	 * Verifica se a mensagem de resposta é válida.
	 * @return true se a mensagem de resposta é válida, caso contrário false.
	 */
	public boolean isValid() {
		return interval != null && trackerId != null && complete != null 
				&& incomplete != null && peers != null;
	}
	
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public Integer getMinInterval() {
		return minInterval;
	}
	public void setMinInterval(Integer minInterval) {
		this.minInterval = minInterval;
	}
	public String getTrackerId() {
		return trackerId;
	}
	public void setTrackerId(String trackerId) {
		this.trackerId = trackerId;
	}
	public Integer getComplete() {
		return complete;
	}
	public void setComplete(Integer complete) {
		this.complete = complete;
	}
	public Integer getIncomplete() {
		return incomplete;
	}
	public void setIncomplete(Integer incomplete) {
		this.incomplete = incomplete;
	}
	public List<Peer> getPeers() {
		return peers;
	}
	public void setPeers(List<Peer> peers) {
		this.peers = peers;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clienteId) {
		this.clientId = clienteId;
	}

	
	// HASHCODE e EQUALS 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((complete == null) ? 0 : complete.hashCode());
		result = prime * result + ((incomplete == null) ? 0 : incomplete.hashCode());
		result = prime * result + ((interval == null) ? 0 : interval.hashCode());
		result = prime * result + ((minInterval == null) ? 0 : minInterval.hashCode());
		result = prime * result + ((peers == null) ? 0 : peers.hashCode());
		result = prime * result + ((trackerId == null) ? 0 : trackerId.hashCode());
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
		ResponseMessage other = (ResponseMessage) obj;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (complete == null) {
			if (other.complete != null)
				return false;
		} else if (!complete.equals(other.complete))
			return false;
		if (incomplete == null) {
			if (other.incomplete != null)
				return false;
		} else if (!incomplete.equals(other.incomplete))
			return false;
		if (interval == null) {
			if (other.interval != null)
				return false;
		} else if (!interval.equals(other.interval))
			return false;
		if (minInterval == null) {
			if (other.minInterval != null)
				return false;
		} else if (!minInterval.equals(other.minInterval))
			return false;
		if (peers == null) {
			if (other.peers != null)
				return false;
		} else if (!peers.equals(other.peers))
			return false;
		if (trackerId == null) {
			if (other.trackerId != null)
				return false;
		} else if (!trackerId.equals(other.trackerId))
			return false;
		return true;
	}

	// TOSTRING
	@Override
	public String toString() {
		return "ResponseMessage [interval=" + interval + ", minInterval=" + minInterval + ", trackerId=" + trackerId
				+ ", complete=" + complete + ", incomplete=" + incomplete + ", peers=" + peers + ", clienteId="
				+ clientId + "]";
	}
	
	
}
