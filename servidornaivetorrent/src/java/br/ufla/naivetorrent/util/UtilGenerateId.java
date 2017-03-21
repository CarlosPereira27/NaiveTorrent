package br.ufla.naivetorrent.util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

public class UtilGenerateId {
	
	/**
	 * Gera um id de 20 bytes usando SHA-1 codificado em hexadecimal.
	 * O ip e a porta são usados como sementes para a geração do id, 
	 * a data e hora atual também além de número aleatórios.
	 * @param ip ip de um peer
	 * @param port porta de um peer 
	 * @return id codificado em hexadecimal
	 */
	public static String generateIdHex(String ip, Integer port) {
		return UtilHex.toHexString(generateId(ip, port));
	}
	
	/**
	 * Gera um id de 20 bytes usando SHA-1. O ip e a porta são usados 
	 * como sementes para a geração do id, a data e hora atual também 
	 * além de número aleatórios.
	 * @param ip ip de um peer
	 * @param port porta de um peer 
	 * @return id de 20 bytes
	 */
	public static ByteBuffer generateId(String ip, Integer port) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("SHA-1");
			Random random = new Random();
			StringBuilder seedId = new StringBuilder();
			seedId.append(ip)
				.append(random.nextLong())
				.append(port)
				.append(random.nextLong())
				.append(new Date())
				.append(random.nextLong());
			return ByteBuffer.wrap(messageDigest.digest(
					seedId.toString().getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gera um id de 20 bytes usando SHA-1 codificado em hexadecimal.
	 * O ip e a porta são usados como sementes para a geração do id.
	 * @param ip ip de um tracker
	 * @param port porta de um tracker 
	 * @return id codificado em hexadecimal
	 */
	public static String generateTrackerIdHex(String hostName, Integer port) {
		return UtilHex.toHexString(generateTrackerId(hostName, port));
	}
	
	/**
	 * Gera um id de 20 bytes usando SHA-1. O ip e a porta são usados 
	 * como sementes para a geração do id.
	 * @param ip ip de um tracker
	 * @param port porta de um tracker 
	 * @return id de 20 bytes
	 */
	public static ByteBuffer generateTrackerId(String hostName, Integer port) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("SHA-1");
			StringBuilder seedId = new StringBuilder();
			seedId.append(hostName)
				.append(port);
			return ByteBuffer.wrap(messageDigest.digest(
					seedId.toString().getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
