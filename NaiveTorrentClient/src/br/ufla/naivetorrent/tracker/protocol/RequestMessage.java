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

}
