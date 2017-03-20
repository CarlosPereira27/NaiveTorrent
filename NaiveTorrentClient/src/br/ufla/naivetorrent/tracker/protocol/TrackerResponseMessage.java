package br.ufla.naivetorrent.tracker.protocol;

import java.io.Serializable;
import java.util.List;

import br.ufla.naivetorrent.domain.peer.Peer;

/**
 * Representa uma resposta do tracker.
 * @author carlos
 *
 */
public class TrackerResponseMessage implements Serializable {

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
	
}
