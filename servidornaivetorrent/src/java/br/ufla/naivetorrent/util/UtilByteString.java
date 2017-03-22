package br.ufla.naivetorrent.util;

import java.nio.ByteBuffer;

public class UtilByteString {
	
	/**
	 * Converte um buffer de bytes em uma string UTF-8, onde cada byte equivale
	 * a um caracter UTF-8.
	 * @param byteBuffer buffer de bytes
	 * @return string em UTF-8 equivalente ao buffer de bytes
	 */
	public String bytesToString(ByteBuffer byteBuffer) {
		byte data[] = byteBuffer.array();
		int length = data.length;
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			char c = (char) (data[i] & 0xFF);
			sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * Converte uma string em um buffer de bytes, onde cada caracter UTF-8 
	 * converte para um byte.
	 * @param string string a ser convertida em bytes
	 * @return buffer de bytes que representa a string
	 */
	public ByteBuffer stringToBytes(String string) {
		char data[] = string.toCharArray();
		int length = data.length;
		byte dataBytes[] = new byte[length];
		for (int i = 0; i < length; i++) {
			dataBytes[i] = (byte) data[i];
		}
		return ByteBuffer.wrap(dataBytes);
	}

}
