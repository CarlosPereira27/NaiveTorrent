package br.ufla.naivetorrent.tracker.protocol;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufla.naivetorrent.domain.peer.Peer;
import gyurix.bencoder.BEncoder;

/**
 * Realiza a decodificação de uma mensagem enviada pelo tracker.
 * @author carlos
 *
 */
public class ResponseMessageDecoder {
	
	/**
	 * Mensagem de resposta codificada em bencoded
	 */
	private String messageStr;
	/**
	 * Mensagem de resposta após decodificação
	 */
	private ResponseMessage responseMessage;
	/**
	 * Mensagem de erro após decodificação
	 */
	private ResponseMessageError responseMessageError;
	
	/**
	 * Construtor de decodificador de mensagens que recebe a mensagem 
	 * a ser decodificada por parâmetro.
	 * @param message mensagem a ser decodificada.
	 */
	public ResponseMessageDecoder(String message) {
		this.messageStr = message;
		decodeMessage();
	}
	
	/**
	 * Decodifica uma mensagem de erro, enviada pelo tracker, em bencoded.
	 * @param dic lista de atributos da mensagem
	 */
	private void decodeMessageError(Map<String, Object> dic) {
		responseMessageError = new ResponseMessageError();
		responseMessageError.setFailureReason((String) 
				dic.get(ResponseAttributes.FAILURE_REASON));
	}
	
	/**
	 * Recupera a lista de peers de uma determinada lista de atributos da 
	 * resposta do tracker.
	 * @param dic lista de atributos da resposta do tracker
	 * @return lista de peers
	 */
	private List<Peer> getListPeers(Map<String, Object> dic) {
		List<Peer> peers = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> peersListDic = (List<Map<String, Object>>) dic.get(
				ResponseAttributes.PEERS);
		for (Map<String, Object> peerDic : peersListDic) {
			Peer peer = new Peer();
			peer.setIdHex((String) peerDic.get(
					ResponseAttributes.PEER_ID));
			String peerIp = (String) peerDic.get(ResponseAttributes.PEER_IP);
			Integer peerPort = (Integer) peerDic.get(ResponseAttributes.PEER_PORT);
			peer.setSocketAddressListening(new InetSocketAddress(peerIp, peerPort));
			peers.add(peer);
		}
		return peers;
	}
	
	/**
	 * Decodifica uma mensagem, enviada pelo tracker, em bencoded.
	 * @param dic lista de atributos da mensagem
	 */
	private void decodeResponseMessage(Map<String, Object> dic) {
		responseMessage = new ResponseMessage();
		responseMessage.setInterval((Integer) 
				dic.get(ResponseAttributes.INTERVAL));
		responseMessage.setMinInterval((Integer) 
				dic.get(ResponseAttributes.MIN_INTERVAL));
		responseMessage.setTrackerId((String) 
				dic.get(ResponseAttributes.TRACKER_ID));
		responseMessage.setComplete((Integer) 
				dic.get(ResponseAttributes.COMPLETE));
		responseMessage.setIncomplete((Integer) 
				dic.get(ResponseAttributes.INCOMPLETE));
		responseMessage.setPeers(getListPeers(dic));
		if (dic.containsKey(ResponseAttributes.CLIENT_ID))
			responseMessage.setClientId((String) 
					dic.get(ResponseAttributes.CLIENT_ID));
	}
	
	/**
	 * Decodifica uma mensagem, enviada pelo tracker, em bencoded.
	 */
	private void decodeMessage() {
		BEncoder bEncoder = new BEncoder(messageStr);
		@SuppressWarnings("unchecked")
		Map<String, Object> dic = (Map<String, Object>) bEncoder.read();
		if (dic.containsKey(ResponseAttributes.FAILURE_REASON)) {
			decodeMessageError(dic);
		} else {
			decodeResponseMessage(dic);
		}
	}
	
	/**
	 * Verifica se a mensagem decodificada é uma mensagem de erro.
	 * @return true, se a mensagem é uma mensagem de erro, caso contrário false
	 */
	public boolean isMessageError() {
		return responseMessageError != null;
	}
	
	/**
	 * Recupera a mensagem decodificada.
	 * @return a mensagem decodificada. Caso a mensagem seja uma mensagem de erro
	 * retorna null
	 */
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	/**
	 * Recupera a mensagem de erro decodificada.
	 * @return a mensagem de erro decodificada. Caso a mensagem não seja uma 
	 * mensagem de erro retorna null
	 */
	public ResponseMessageError getResponseMessageError() {
		return responseMessageError;
	}
	
	

}
