package br.ufla.naivetorrent.util;

import java.nio.ByteBuffer;

public class UtilHex {

	static final char[] HEX_SYMBOLS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
			'A', 'B', 'C', 'D', 'E', 'F' };
	

	/***
	 * Converte um array de bytes em uma string em hexadecimal.
	 * @param buffer buffer que contém array de bytes
	 * @return string em hexadecimal
	 */
	public static String toHexString(ByteBuffer buffer) {
		byte bytes[] = buffer.array();
		int n = bytes.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			int intVal;
			if (bytes[i] < 0)
				intVal = 256 + bytes[i];
			else
				intVal = bytes[i];
			sb.append(HEX_SYMBOLS[intVal >>> 4])
				.append(HEX_SYMBOLS[intVal & 0x0F]);
		}
		return sb.toString();	
	}
	
	/**
	 * Converte uma string em hexadecimal em um array de bytes contidos em um buffer.
	 * @param hexString string em hexadecimal
	 * @return buffer com array de bytes convertidos
	 */
	public static ByteBuffer toBytes(String hexString) {
		int length = hexString.length() / 2;
		byte bytes[] = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = (byte) ( getValueE(hexString.charAt(2 * i)) + 
					getValueD(hexString.charAt(2 * i + 1)) );
		}
		return ByteBuffer.wrap(bytes);	
	}
	
	/**
	 * Recupera o valor de um caractere hexadecimal da representação de um byte, este método
	 * recupera o valor dos 4 bits mais a direita de um byte.
	 * @param c caractere hexadecimal representado os 4 bits mais a direita de um byte
	 * @return valor dos 4 bits mais a direita de um byte 
	 */
	private static int getValueD(char c) {
		if (Character.isDigit(c))
			return c - 48;
		return c - 55;
	}
	
	/**
	 * Recupera o valor de um caractere hexadecimal da representação de um byte, este método
	 * recupera o valor dos 4 bits mais a esquerda de um byte.
	 * @param c caractere hexadecimal representado os 4 bits mais a esquerda de um byte
	 * @return valor dos 4 bits mais a esquerda de um byte 
	 */
	private static int getValueE(char c) {
		int val = getValueD(c) << 4;
		if (val > 127)
			return val - 256;
		return val;
	}

}
