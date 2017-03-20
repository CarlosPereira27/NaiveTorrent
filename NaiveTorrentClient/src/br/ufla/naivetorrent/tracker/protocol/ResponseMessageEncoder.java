package br.ufla.naivetorrent.tracker.protocol;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ufla.naivetorrent.domain.peer.Peer;
import gyurix.bencoder.BEncoder;

/**
 * Realiza a codificação de uma mensagem de resposta do tracker.
 * @author carlos
 *
 */
public class ResponseMessageEncoder {
	
	/**
	 * Mensagem de resposta
	 */
	private ResponseMessage responseMessage;
	/**
	 * Mensagem de resposta codificada em bencoded
	 */
	private String messageEncode;

	/**
	 * Construtor de codificador bencoded de mensagem de resposta, recebe a mensagem 
	 * de resposta a ser codificada por parâmetro.
	 * @param responseMessage mensagem de resposta a ser codificada
	 */
	public ResponseMessageEncoder(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
		encode();
	}
	
	/**
	 * Codifica a lista de peers usando bencoded.
	 * @return lista de peers codificada com bencoded
	 */
	private List<Map<String, Object>> encodeListPeers() {
		List<Map<String, Object>> peersListDic = new ArrayList<>();
		List<Peer> peers = responseMessage.getPeers();
		for (Peer peer : peers) {
			Map<String, Object> peerDic = new LinkedHashMap<>();
			peerDic.put(ResponseAttributes.PEER_ID, peer.getIdHex());
			peerDic.put(ResponseAttributes.PEER_IP, peer.getIp());
			peerDic.put(ResponseAttributes.PEER_PORT, peer.getPort());
			peersListDic.add(peerDic);
		}
		return peersListDic;
	}
	
	/**
	 * Codifica a mensagem de resposta em bencoded.
	 */
	private void encode() {
		if (!responseMessage.isValid()) {
			throw new RuntimeException("Mensagem de resposta não é válida.");
		}
		Map<String, Object> dic = new LinkedHashMap<>();
		dic.put(ResponseAttributes.INTERVAL, responseMessage.getInterval());
		if (responseMessage.getMinInterval() != null) {
			dic.put(ResponseAttributes.MIN_INTERVAL, responseMessage.getMinInterval());
		}
		dic.put(ResponseAttributes.TRACKER_ID, responseMessage.getTrackerId());
		dic.put(ResponseAttributes.COMPLETE, responseMessage.getComplete());
		dic.put(ResponseAttributes.INCOMPLETE, responseMessage.getIncomplete());
		dic.put(ResponseAttributes.PEERS, encodeListPeers());
		BEncoder bEncoder = new BEncoder();
		bEncoder.write(dic);
		messageEncode = bEncoder.toString();
	}
	
	/**
	 * Recupera a mensagem de resposta codifica em bencoded.
	 * @return mensagem de resposta codifica em bencoded
	 */
	public String getMessageEncode() {
		return messageEncode;
	}

}
