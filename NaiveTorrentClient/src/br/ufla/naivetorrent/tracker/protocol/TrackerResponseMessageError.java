package br.ufla.naivetorrent.tracker.protocol;

import java.io.Serializable;

/**
 * Representa uma resposta de falha do tracker.
 * @author carlos
 *
 */
public class TrackerResponseMessageError implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * Texto explicando razão da falha na requisição.
	 */
	private String failureReason;
	
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	
	
}
