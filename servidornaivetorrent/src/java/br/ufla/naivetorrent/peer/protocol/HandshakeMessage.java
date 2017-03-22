package br.ufla.naivetorrent.peer.protocol;

import java.io.Serializable;
import java.net.ProtocolException;
import java.nio.ByteBuffer;

import br.ufla.naivetorrent.util.UtilByteString;

public class HandshakeMessage implements Serializable {
	
	public static final String PROTOCOL = "NaiveTorrent protocol";
	public static final int MESSAGE_LENGTH = 45 + PROTOCOL.length();
	private static final long serialVersionUID = 1L;
	/**
	 * Tamanho da string de identificação de protocol
	 */
	private Byte pstrlen;
	/**
	 * String de identificação de protocolo
	 */
	private String pstr;
	/**
	 * 8 bytes reservados para mudança no protocolo. Deve utilizar todos os
	 * bytes 0
	 */
	private ByteBuffer reserved;
	/**
	 * Hash do arquivo que deseja compartilhar
	 */
	private ByteBuffer infoHash;
	/**
	 * Identificação do peer
	 */
	private ByteBuffer peerId;
	
	public HandshakeMessage(ByteBuffer infoHash, ByteBuffer peerId) {
		pstrlen = (byte) PROTOCOL.length();
		pstr = PROTOCOL;
		reserved = ByteBuffer.wrap(new byte[8]);
		this.infoHash = infoHash;
		this.peerId = peerId;
	}
	
	private HandshakeMessage() {
		
	}
	
	/**
	 * Decodifica uma mensagem 
	 * @param data
	 * @return
	 */
	public static HandshakeMessage decode(byte data[]) {
		if (data.length != MESSAGE_LENGTH) {
			return null;
		}
		HandshakeMessage handshakeMessage = new HandshakeMessage();
		int ind = 0;
		handshakeMessage.pstrlen = data[ind++];
		handshakeMessage.pstr = UtilByteString.bytesToString(ByteBuffer.wrap(data,
				ind, handshakeMessage.pstrlen));
		ind += handshakeMessage.pstrlen;
		handshakeMessage.reserved = ByteBuffer.wrap(data, ind, 8);
		ind += 8;
		handshakeMessage.infoHash = ByteBuffer.wrap(data, ind, 16);
		ind += 16;
		handshakeMessage.infoHash = ByteBuffer.wrap(data, ind, 20);
		ind += 20;
		return handshakeMessage;
	}
	
	/**
	 * Recupera o tamanho da mensagem em bytes.
	 * @return tamanho da mensagem em bytes
	 */
	private int getLength() {
		return 1 + pstr.length() + reserved.array().length
				+ infoHash.array().length + peerId.array().length;
	}
	
	/**
	 * Insere os dados do array dataAux em data a partir do índice ind.
	 * @param data dados principal para onde dataAux será copiado
	 * @param dataAux dados que serão copiados
	 * @param ind índice onde será iniciado a copia
	 * @return índice atual
	 */
	private int insert(byte data[], byte dataAux[], int ind) {
		int length = dataAux.length;
		for (int i = 0; i < length; i++) {
			data[ind] = dataAux[i];
			ind++;
		}
		return ind;
	}
	
	/**
	 * Retorna a mensagem como um array de bytes.
	 * @return mensagem em um array de bytes
	 */
	public byte[] toByteArray() {
		byte data[] = new byte[getLength()];
		int ind = 0;
		data[ind] = pstrlen;
		ind = insert(data, UtilByteString.stringToBytes(pstr)
				.array(), ind);
		ind = insert(data, reserved.array(), ind);
		ind = insert(data, infoHash.array(), ind);
		ind = insert(data, peerId.array(), ind);
		return data;
	}
	
	public Byte getPstrlen() {
		return pstrlen;
	}
	public void setPstrlen(Byte pstrlen) {
		this.pstrlen = pstrlen;
	}
	public String getPstr() {
		return pstr;
	}
	public void setPstr(String pstr) {
		this.pstr = pstr;
	}
	public ByteBuffer getReserved() {
		return reserved;
	}
	public void setReserved(ByteBuffer reserved) {
		this.reserved = reserved;
	}
	public ByteBuffer getInfoHash() {
		return infoHash;
	}
	public void setInfoHash(ByteBuffer infoHash) {
		this.infoHash = infoHash;
	}
	public ByteBuffer getPeerId() {
		return peerId;
	}
	public void setPeerId(ByteBuffer peerId) {
		this.peerId = peerId;
	}
	
	// HASHCODE e EQUALS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((infoHash == null) ? 0 : infoHash.hashCode());
		result = prime * result + ((peerId == null) ? 0 : peerId.hashCode());
		result = prime * result + ((pstr == null) ? 0 : pstr.hashCode());
		result = prime * result + ((pstrlen == null) ? 0 : pstrlen.hashCode());
		result = prime * result + ((reserved == null) ? 0 : reserved.hashCode());
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
		HandshakeMessage other = (HandshakeMessage) obj;
		if (infoHash == null) {
			if (other.infoHash != null)
				return false;
		} else if (!infoHash.equals(other.infoHash))
			return false;
		if (peerId == null) {
			if (other.peerId != null)
				return false;
		} else if (!peerId.equals(other.peerId))
			return false;
		if (pstr == null) {
			if (other.pstr != null)
				return false;
		} else if (!pstr.equals(other.pstr))
			return false;
		if (pstrlen == null) {
			if (other.pstrlen != null)
				return false;
		} else if (!pstrlen.equals(other.pstrlen))
			return false;
		if (reserved == null) {
			if (other.reserved != null)
				return false;
		} else if (!reserved.equals(other.reserved))
			return false;
		return true;
	}
	
	// TOSTRING
	@Override
	public String toString() {
		return "HandshakeMessage [pstrlen=" + pstrlen + ", pstr=" + pstr + ", reserved=" + reserved + ", infoHash="
				+ infoHash + ", peerId=" + peerId + "]";
	}
	
	

}
