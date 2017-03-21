package br.ufla.naivetorrent.tracker.protocol;

import java.util.LinkedHashMap;
import java.util.Map;

import gyurix.bencoder.BEncoder;

/**
 * Realiza a codificação de uma mensagem de erro do tracker.
 * @author carlos
 *
 */
public class ResponseMessageErrorEncoder {
	
	/**
	 * Mensagem de erro
	 */
	private ResponseMessageError responseMessageError;
	/**
	 * Mensagem de erro codificada em bencoded
	 */
	private String messageErrorEncode;

	/**
	 * Construtor de codificador bencoded de mensagem de erro, recebe a mensagem 
	 * de erro a ser codificada por parâmetro.
	 * @param responseMessage mensagem de erro a ser codificada
	 */
	public ResponseMessageErrorEncoder(ResponseMessageError responseMessageError) {
		this.responseMessageError = responseMessageError;
		encode();
	}
	
	/**
	 * Codifica a mensagem de erro em bencoded.
	 */
	private void encode() {
		if (!responseMessageError.isValid()) {
			throw new RuntimeException("Mensagem de erro não é válida.");
		}
		Map<String, Object> dic = new LinkedHashMap<>();
		dic.put(ResponseAttributes.FAILURE_REASON, responseMessageError.getFailureReason());
		BEncoder bEncoder = new BEncoder();
		bEncoder.write(dic);
		messageErrorEncode = bEncoder.toString();
	}
	
	/**
	 * Recupera a mensagem de erro codifica em bencoded.
	 * @return mensagem de erro codifica em bencoded
	 */
	public String getMessageErrorEncode() {
		return messageErrorEncode;
	}

}
