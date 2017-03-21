package br.ufla.naivetorrent.tracker.protocol;

import java.io.Serializable;

/**
 * Representa uma resposta de falha do tracker.
 * @author carlos
 *
 */
public class ResponseMessageError implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * Texto explicando razão da falha na requisição.
	 */
	private String failureReason;
	
	
	/**
	 * Verifica se a mensagem de erro é válida.
	 * @return true se a mensagem de erro é válida, caso contrário false.
	 */
	public boolean isValid() {
		return failureReason != null;
	}
	
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	
	
}
