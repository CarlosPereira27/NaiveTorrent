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

	
	// HASHCODE e EQUALS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((failureReason == null) ? 0 : failureReason.hashCode());
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
		ResponseMessageError other = (ResponseMessageError) obj;
		if (failureReason == null) {
			if (other.failureReason != null)
				return false;
		} else if (!failureReason.equals(other.failureReason))
			return false;
		return true;
	}

	// TOSTRING
	@Override
	public String toString() {
		return "ResponseMessageError [failureReason=" + failureReason + "]";
	}
	
	
	
}
